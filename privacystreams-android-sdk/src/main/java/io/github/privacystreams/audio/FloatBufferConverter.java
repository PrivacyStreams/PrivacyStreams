package io.github.privacystreams.audio;

import java.util.List;

public class FloatBufferConverter {
    double[] result;

    public FloatBufferConverter(List<Byte> byteBuffer){
        double[] floatbuffer = new double[1024];
        int c = 0;
        while (c < 1024) {
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
        result = floatbuffer;
    }

    public FloatBufferConverter(List<Byte> byteBuffer, int frameSize){
        double[] floatbuffer = new double[frameSize];
        int c = 0;
        while (c < frameSize) {
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
        result=floatbuffer;
    }

    public FloatBufferConverter(byte[] byteBuffer){
        double[] floatbuffer = new double[1024];
        int c = 0;
        while (c < 1024) {
            int index = c * 4;
            int first = byteBuffer[index];
            int second = byteBuffer[index+1];
            int third = byteBuffer[index+2];
            int fourth = byteBuffer[index+3];
            float number = first + second * 256 + third * 256 * 256 + fourth * 256 * 256 * 256;
            double pcmfloat = (number / (256 * 256 * 256 * 128));
            floatbuffer[c] = pcmfloat;
            c = c + 1;
        }
        result = floatbuffer;
    }
}
