package io.github.privacystreams.audio;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Pitch_YIN {

    double threshold = 0.20;
    final List<Byte> bytebuffer;
    final String FilePath;
    final int frameSize;
    List<Double> result = new ArrayList<>();
    float[] difference_list;

    public Pitch_YIN(String Path){
        FilePath = Path;
        bytebuffer = null;
        frameSize = 1024;
        difference_list = new float[frameSize/2];
    }

    public Pitch_YIN(List<Byte> ByteBuffer) {
        FilePath = null;
        bytebuffer = ByteBuffer;
        frameSize = 1024; difference_list = new float[frameSize/2];
    }

    public Pitch_YIN(List<Byte> ByteBuffer, int bytesInFrame) {
        FilePath = null;
        bytebuffer = ByteBuffer;
        frameSize = bytesInFrame;
        difference_list = new float[frameSize/2];
    }

    public void Process(){
        if (bytebuffer != null){
            List<Byte> temp = bytebuffer;
            while (temp.size() > 0) {
                List<Byte> singleframe = temp.subList(0, frameSize*4);
                temp = temp.subList(frameSize*4, temp.size());
                double[] floatbuffer = (new FloatBufferConverter(singleframe)).result;
                double pitch = getPitch(floatbuffer);
                result.add(pitch);
            }
        }else{
            getFilePitch();
        }

    }

    public void getFilePitch(){
        try{
            File newFile = new File(FilePath);

            byte[] buffer = new byte[frameSize*4];
            byte[] fileheader = new byte[44];

            FileInputStream inStream = new FileInputStream(newFile);

            int nRead;
            double[] floatbuffer;

            inStream.read(fileheader);

            while ((nRead = inStream.read(buffer)) != -1) {
                floatbuffer = (new FloatBufferConverter(buffer)).result;
                double pitch = getPitch(floatbuffer);
                result.add(pitch);
            }

        }catch(Exception e){e.printStackTrace();}
    }


    public double getPitch(double[] audioData) {

        final int tauEstimate;
        final float pitchInHertz;

        difference(audioData);

        cumulativeMeanNormalizedDifference();


        tauEstimate = absoluteThreshold();

        if (tauEstimate != -1) {
            final float betterTau = parabolicInterpolation(tauEstimate);
            pitchInHertz = 44100 / betterTau;
        } else{
            // no pitch found
            pitchInHertz = -1;
        }



        return pitchInHertz;
    }


    public void difference(final double[] audioBuffer) {
        int index, tau;
        double delta;
        for (tau = 0; tau < difference_list.length; tau++) {
            difference_list[tau] = 0;
        }
        for (tau = 1; tau < difference_list.length; tau++) {
            for (index = 0; index < difference_list.length; index++) {
                delta = audioBuffer[index] - audioBuffer[index + tau];
                difference_list[tau] += delta * delta;
            }
        }
    }


    private void cumulativeMeanNormalizedDifference() {
        int tau;
        difference_list[0] = 1;
        float runningSum = 0;
        for (tau = 1; tau < difference_list.length; tau++) {
            runningSum += difference_list[tau];
            difference_list[tau] *= tau / runningSum;
        }
    }


    private int absoluteThreshold() {

        int tau;
        float min_value = difference_list[2];
        int min = 2;
        boolean found_deeper_value = false;

        for (tau = 2; tau < difference_list.length; tau++) {

            if (difference_list[tau] < threshold) {
                found_deeper_value = true;
                if (difference_list[tau]<min_value){
                    min_value = difference_list[tau];
                    min = tau;
                }

                while (tau + 1 < difference_list.length && difference_list[tau + 1] < difference_list[tau]) {
                    if (difference_list[tau]<min_value){
                        min_value = difference_list[tau];
                        min = tau;
                    }
                    tau++;

                }

                break;
            }
        }

        if (tau == difference_list.length){
            tau = tau-1;
        }
        if (!found_deeper_value){
            return min;
        }else{
            return tau;
        }

    }
    private float parabolicInterpolation(final int tauEstimate) {
        final float betterTau;
        final int x0;
        final int x2;

        if (tauEstimate < 1) {
            x0 = tauEstimate;
        } else {
            x0 = tauEstimate - 1;
        }
        if (tauEstimate + 1 < difference_list.length) {
            x2 = tauEstimate + 1;
        } else {
            x2 = tauEstimate;
        }

        if (x0 == tauEstimate) {
            if (difference_list[tauEstimate] <= difference_list[x2]) {
                betterTau = tauEstimate;
            } else {
                betterTau = x2;
            }
        } else if (x2 == tauEstimate) {
            if (difference_list[tauEstimate] <= difference_list[x0]) {
                betterTau = tauEstimate;
            } else {
                betterTau = x0;
            }
        } else {
            float s0, s1, s2;
            s0 = difference_list[x0];
            s1 = difference_list[tauEstimate];
            s2 = difference_list[x2];
            betterTau = tauEstimate + (s2 - s0) / (2 * (2 * s1 - s2 - s0));
        }
        return betterTau;
    }
}
