package io.github.privacystreams.audio;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.sin;

public class ZCRCalculator {
    int frameSize;
    final List<Short> audioData;
    String FilePath;
    List<Double> result = new ArrayList<>();

    public ZCRCalculator(String File){
        frameSize=1024;
        FilePath = File;
        audioData = null;

    }
    public ZCRCalculator(List<Short> data){
        this(data, 1024);
    }

    public ZCRCalculator(List<Short> data, int samplesPerFrame){
        FilePath = null;
        audioData = data;
        frameSize = samplesPerFrame;

    }

    public void Process() {
        if (FilePath != null) {
            try {
                File newFile = new File(FilePath);

                byte[] buffer = new byte[frameSize * 4];
                byte[] fileheader = new byte[44];

                FileInputStream inStream = new FileInputStream(newFile);

                int nRead;
                double[] floatbuffer;

                inStream.read(fileheader);

                while ((nRead = inStream.read(buffer)) != -1) {
                    floatbuffer = (new FloatBufferConverter(buffer)).result;
                    double ZCR = getZCR(floatbuffer);
                    result.add(ZCR);
                }
            } catch (Exception e) {e.printStackTrace(); }
        } else{
            List<Short> temp = audioData;
            while (temp.size() > 0) {
                List<Short> singleframe = temp.subList(0, frameSize);
                temp = temp.subList(frameSize, temp.size());
                double[] floatbuffer = (new FloatBufferConverter(singleframe)).result;
                double ZCR = getZCR(floatbuffer);
                result.add(ZCR);
            }
        }
    }

    public double getZCR(double[] audioData){
        int length = audioData.length;
        int sum = 0;

        for (int i=0; i<length-1; i++){
            int sign1;
            int sign2;
            if (audioData[i] >=0){
                sign1=1;
            }else{
                sign1=-1;
            }

            if (audioData[i+1] >= 0) {
                sign2=1;
            }else{
                sign2=-1;
            }

            sum += abs(sign1-sign2);

        }

        double rate = sum / (double)(2*length);

        return rate;
    }
}
