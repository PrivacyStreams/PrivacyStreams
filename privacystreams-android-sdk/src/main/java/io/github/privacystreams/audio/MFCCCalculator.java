package io.github.privacystreams.audio;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.FastFourierTransformer;


import static org.apache.commons.math3.transform.DftNormalization.STANDARD;
import static org.apache.commons.math3.transform.DftNormalization.UNITARY;
import static org.apache.commons.math3.transform.TransformType.FORWARD;

public class MFCCCalculator {
    int frameSize;
    int amountOfCepstrumCoef;
    int amountOfMelFilters;
    final float lowerFilterFreq = 133.3334f;
    final float upperFilterFreq = 44100 / 2f;
    final int sampleRate = 44100;
    int[] centerFrequencies;
    List<Short> audioData;
    List<Double[]> result = new ArrayList<>();

    String filePath;

    public MFCCCalculator(String File) {
        filePath = File;
        audioData = null;
        frameSize = 1024;
        amountOfCepstrumCoef = 30;
        amountOfMelFilters = 30;
    }

    public MFCCCalculator(List<Short> Data) {
        this(Data, 1024, 30, 30);

    }

    public MFCCCalculator(List<Short> Data, int samplesPerFrame) {
        this(Data, samplesPerFrame, 30, 30);
    }


    public MFCCCalculator(List<Short> Data, int samplesPerFrame, int MelFilters, int CepstrumCoef) {
        audioData = Data;
        filePath = null;
        amountOfCepstrumCoef = CepstrumCoef;
        amountOfMelFilters = MelFilters;
        frameSize = samplesPerFrame;
    }

    public void process() {
        if (filePath != null) {
            try {
                File newFile = new File(filePath);

                byte[] buffer = new byte[frameSize * 4];
                byte[] fileheader = new byte[44];

                FileInputStream inStream = new FileInputStream(newFile);

                int nRead;
                double[] floatBuffer;

                inStream.read(fileheader);

                while ((nRead = inStream.read(buffer)) != -1) {
                    floatBuffer = (new FloatBufferConverter(buffer)).result;
                    Double[] mfcc = getMFCC(floatBuffer);

                    result.add(mfcc);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            List<Short> temp = audioData;
            double[] floatbuffer = new double[1];
            while (temp.size() > 0) {
                if (temp.size() < frameSize){
                    break;
                }
                List<Short> singleframe = temp.subList(0, frameSize);
                temp = temp.subList(frameSize, temp.size());
                floatbuffer = (new FloatBufferConverter(singleframe)).result;

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
        // Non-linear transformation
        float f[] = nonLinearTransformation(fbank);
        // Cepstral coefficients
        Double[] MFCC = DCT(f);

        return MFCC;
    }

    public final void calculateFilterBanks() {
        int points = amountOfMelFilters + 2;
        centerFrequencies = new int[points];

        centerFrequencies[0] = Math.round(lowerFilterFreq / sampleRate * frameSize);
        centerFrequencies[points - 1] = Math.round(upperFilterFreq / sampleRate * frameSize);

        double lowerMel = freqToMel(lowerFilterFreq);
        double higherMel = freqToMel(upperFilterFreq);


        float interval = (float) ((higherMel - lowerMel) / (amountOfMelFilters + 1));
        for (int i = 1; i <= amountOfMelFilters; i++) {
            float freq = (inverseMel(lowerMel + interval * i) / sampleRate) * frameSize;
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
            cepc[i] = 0.0;
            for (int j = 0; j < length; j++) {
                cepc[i] += f[j] * Math.cos(Math.PI * i / length * (j+ 0.5));
            }
        }

        return cepc;
    }
}







