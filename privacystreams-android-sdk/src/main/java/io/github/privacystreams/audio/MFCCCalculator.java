package io.github.privacystreams.audio;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.FastFourierTransformer;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.apache.commons.math3.transform.DftNormalization.STANDARD;
import static org.apache.commons.math3.transform.TransformType.FORWARD;

public class MFCCCalculator {

    int samplesPerFrame;
    int amountOfCepstrumCoef;
    int amountOfMelFilters;
    final float lowerFilterFreq = 133.3334f;
    final float upperFilterFreq = 44100 / 2f;
    final int sampleRate = 44100;
    int[] centerFrequencies;
    List<Byte> bytebuffer;
    List<Double[]> result;

    String filePath;

    public MFCCCalculator(String File) {
        filePath = File;
        samplesPerFrame = 1024;
        amountOfCepstrumCoef = 30;
        amountOfMelFilters = 30;
    }

    public MFCCCalculator(List<Byte> byteBuffer) {
        this(byteBuffer, 1024, 30, 30);
        bytebuffer = byteBuffer;

    }

    public MFCCCalculator(List<Byte> bytebuffer, int framsize, int MelFilters, int CepstrumCoef) {
        amountOfCepstrumCoef = CepstrumCoef;
        amountOfMelFilters = MelFilters;
        samplesPerFrame = framsize;
    }

    public MFCCCalculator(List<Byte> bytebuffer, int framsize) {
       this(bytebuffer, framsize, 30, 30);
    }

    public double[] computeFloatBuffer(byte[] byteBuffer) {
        double[] floatbuffer = new double[samplesPerFrame];
        int c = 0;
        while (c < samplesPerFrame) {
            int index = c * 4;
            int first = byteBuffer[index];
            int second = byteBuffer[index + 1];
            int third = byteBuffer[index + 2];
            int fourth = byteBuffer[index + 3];
            float number = first + second * 256 + third * 256 * 256 + fourth * 256 * 256 * 256;
            double pcmfloat = (number / (256 * 256 * 256 * 128));
            floatbuffer[c] = pcmfloat;
            c = c + 1;
        }

        return floatbuffer;
    }

    public double[] computeFloatBuffer(List<Byte> byteBuffer) {
        double[] floatbuffer = new double[samplesPerFrame];
        int c = 0;
        while (c < samplesPerFrame) {
            int index = c * 4;
            int first = byteBuffer.get(index);
            int second = byteBuffer.get(index + 1);
            int third = byteBuffer.get(index + 2);
            int fourth = byteBuffer.get(index + 3);
            float number = first + second * 256 + third * 256 * 256 + fourth * 256 * 256 * 256;
            double pcmfloat = (number / (256 * 256 * 256 * 128));
            floatbuffer[c] = pcmfloat;
            c = c + 1;
        }

        return floatbuffer;
    }

    public void Process() {
        if (filePath != null) {
            try {
                File newFile = new File(filePath);

                byte[] bytebuffer = new byte[samplesPerFrame * 4];
                byte[] fileheader = new byte[44];

                FileInputStream inStream = new FileInputStream(newFile);

                int nRead;
                double[] floatBuffer;

                inStream.read(fileheader);

                while ((nRead = inStream.read(bytebuffer)) != -1) {
                    floatBuffer = computeFloatBuffer(bytebuffer);
                    Double[] mfcc = getMFCC(floatBuffer);
                    result.add(mfcc);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            List<Byte> temp = bytebuffer;
            while (temp.size() > 0) {
                List<Byte> singleframe = temp.subList(0, samplesPerFrame * 4);
                temp = temp.subList(samplesPerFrame * 4, temp.size());
                double[] floatbuffer = computeFloatBuffer(singleframe);
                Double[] mfcc = getMFCC(floatbuffer);
                result.add(mfcc);
            }
        }

    }

    public static float HammingWindow(int length, int index) {
        return 0.54f - 0.46f * (float) Math.cos(2 * Math.PI * index / (length - 1));
    }

    public Double[] getMFCC(double[] floatBuffer) {

        calculateFilterBanks();
        // Magnitude Spectrum
        FastFourierTransformer newTransform = new FastFourierTransformer(STANDARD);

        for (int i = 0; i < floatBuffer.length; i++) {
            floatBuffer[i] = floatBuffer[i] * HammingWindow(1024, i);
        }
        Complex complex_bin[] = newTransform.transform(floatBuffer, FORWARD);

        double bin[] = new double[complex_bin.length];
        for (int i = 0; i < complex_bin.length; i++) {
            bin[i] = complex_bin[i].abs();
        }

        // get Mel Filterbank
        float fbank[] = melFilter(bin);
        for (float e : centerFrequencies) {
            System.out.println(e);
        }
        // Non-linear transformation
        float f[] = nonLinearTransformation(fbank);
        // Cepstral coefficients
        Double[] MFCC = DCT(f);

        return MFCC;
    }

    public final void calculateFilterBanks() {
        int points = amountOfMelFilters + 2;
        centerFrequencies = new int[points];

        centerFrequencies[0] = Math.round(lowerFilterFreq / sampleRate * samplesPerFrame);
        centerFrequencies[points - 1] = Math.round(upperFilterFreq / sampleRate * samplesPerFrame);

        double lowerMel = freqToMel(lowerFilterFreq);
        double higherMel = freqToMel(upperFilterFreq);


        float interval = (float) ((higherMel - lowerMel) / (amountOfMelFilters + 1));
        for (int i = 1; i <= amountOfMelFilters; i++) {
            float freq = (inverseMel(lowerMel + interval * i) / sampleRate) * samplesPerFrame;
            centerFrequencies[i - 1] = Math.round(freq);
        }

    }

    private float inverseMel(double m) {
        return (float) (700 * (Math.pow(10, m / 2595) - 1));
    }

    protected float freqToMel(float freq) {
        return (float) (1125 * Math.log(1 + freq / 700));
    }

    public float[] melFilter(double bin[]) {
        int amountOfMelFilters = 30;
        float temp[] = new float[amountOfMelFilters + 2];

        for (int k = 1; k <= amountOfMelFilters; k++) {
            float num1 = 0, num2 = 0;

            float den = (centerFrequencies[k] - centerFrequencies[k - 1] + 1);

            for (int i = centerFrequencies[k - 1]; i <= centerFrequencies[k]; i++) {
                num1 += bin[i] * (i - centerFrequencies[k - 1] + 1);
            }
            num1 /= den;

            den = (centerFrequencies[k + 1] - centerFrequencies[k] + 1);

            for (int i = centerFrequencies[k] + 1; i <= centerFrequencies[k + 1]; i++) {
                num2 += bin[i] * (1 - ((i - centerFrequencies[k]) / den));
            }

//            float denominator1 = (centerFrequencies[k]-centerFrequencies[k-1]);
//            float denominator2 = (centerFrequencies[k+1]-centerFrequencies[k]);
//
//            for (int i=centerFrequencies[k-1]; i<=centerFrequencies[k]; i++) {
//                num1 += bin[i]*(i-centerFrequencies[k-1]);
//            }
//            num1 /= denominator1;
//
//            for (int i=centerFrequencies[k]; i<=centerFrequencies[k+1]; i++) {
//                num2 += bin[i]*(centerFrequencies[k+1]-i);
//            }
//            num2 /= denominator2;

            temp[k] = num1 + num2;
        }

        float fbank[] = new float[amountOfMelFilters];

        for (int i = 0; i < amountOfMelFilters; i++) {
            fbank[i] = temp[i + 1];
        }

        return fbank;
    }


    public float[] nonLinearTransformation(float fbank[]) {
        float f[] = new float[fbank.length];

        for (int i = 0; i < fbank.length; i++) {
            f[i] = (float) Math.log(fbank[i]);

        }

        return f;
    }

    public Double[] DCT(float f[]) {
        int length = f.length;
        Double cepc[] = new Double[amountOfCepstrumCoef];

        for (int i = 0; i < amountOfCepstrumCoef; i++) {
            for (int j = 0; j < f.length; j++) {
                cepc[i] += f[j] * Math.cos(Math.PI * i / length * (j + 0.5));
            }
        }

        return cepc;
    }
}
