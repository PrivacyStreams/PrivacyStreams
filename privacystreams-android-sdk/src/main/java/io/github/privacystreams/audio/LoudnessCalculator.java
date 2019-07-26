package io.github.privacystreams.audio;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static java.lang.Math.abs;

public class LoudnessCalculator {
    String FilePath;
    List<Short> audioData;
    int sum = 0;
    int count = 0;
    int maximumAmplitude;
    double averageLoudness;
    int temp_largest = -256;

    public LoudnessCalculator(String Path){
        FilePath = Path;
        audioData = null;
    }

    public LoudnessCalculator(List<Short> Data){
        audioData = Data;
        FilePath = null;

    }

    public int[] ShortBufferConverter(byte[] byteBuffer){
        int[] tempbuffer = new int[byteBuffer.length/2];
        int c = 0;
        while (c < byteBuffer.length/2){
            int index = c*2;
            int first = byteBuffer[index];
            int second = byteBuffer[index+1];
            int value = first + second*256;
            tempbuffer[c] = value;
            c+=1;
        }

        return tempbuffer;
    }


    public void Process(){
        double sum = 0;
        if (FilePath != null) {
            try {
                File newFile = new File(FilePath);

                byte[] buffer = new byte[4096];
                byte[] fileheader = new byte[44];

                FileInputStream inStream = new FileInputStream(newFile);

                int nRead;
                int[] temp;

                inStream.read(fileheader);

                while ((nRead = inStream.read(buffer)) != -1) {
                    temp = ShortBufferConverter(buffer);
                    computeSum(temp);
                }

                double averageAmplitude = sum/count;

                averageLoudness = convertToLoudeness(averageAmplitude);
                maximumAmplitude = temp_largest;

            } catch (Exception e) {e.printStackTrace(); }
        } else{
            List<Short> temp = audioData;
            computeSum(temp);
            double averageAmplitude = sum/count;

            averageLoudness = convertToLoudeness(averageAmplitude);
            maximumAmplitude = temp_largest;
        }
    }

    public void computeSum(List<Short> list){
        for (Short e : list){
            if ( Math.abs(e) >temp_largest){
                temp_largest = e;
            }
            sum += Math.abs(e);

            if (e != 0){
                count += 1;
            }

        }
    }
    public void computeSum(int[] list){
        for (int e : list){
            if ( Math.abs(e) >temp_largest){
                temp_largest = e;
            }
            sum += Math.abs(e);

            if (e != 0){
                count += 1;
            }

        }
    }

    public double convertToLoudeness(double amplitude){
        return  20 * Math.log10(amplitude/51805/0.00002);
    }

}

