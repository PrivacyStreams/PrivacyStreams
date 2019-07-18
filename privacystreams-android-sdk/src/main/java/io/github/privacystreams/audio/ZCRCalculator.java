package io.github.privacystreams.audio;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.abs;

public class ZCRCalculator {
    int frameSize;
    final List<Byte> bytebuffer;
    String FilePath;
    List<Double> result = new ArrayList<>();

    public ZCRCalculator(String File){
        frameSize=1024;
        FilePath = File;
        bytebuffer = null;

    }
    public ZCRCalculator(List<Byte> byteBuffer){
        this(byteBuffer, 1024);
    }

    public ZCRCalculator(List<Byte> byteBuffer, int samplesPerFrame){
        FilePath = null;
        bytebuffer = byteBuffer;
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
            List<Byte> temp = bytebuffer;
            while (temp.size() > 0) {
                List<Byte> singleframe = temp.subList(0, frameSize * 4);
                temp = temp.subList(frameSize * 4, temp.size());
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
