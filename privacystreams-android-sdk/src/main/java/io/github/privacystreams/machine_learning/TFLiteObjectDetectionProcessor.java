package io.github.privacystreams.machine_learning;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.ImageUtils;

class TFLiteObjectDetectionProcessor extends MLProcessor<Object[]>{
    Bitmap bitmap;
    String inputField;
    ByteBuffer imgData;
    int inputSize;
    int[] intValues;
    boolean isModelQuantized;
    private static final float IMAGE_MEAN = 128.0f;
    private static final float IMAGE_STD = 128.0f;


    TFLiteObjectDetectionProcessor(String inputField, int inputSize, boolean isQuantized){
        super(Arrays.asList(new String[]{inputField}));
        this.inputField = inputField;
        this.addParameters(inputField);
        this.inputSize = inputSize;
        this.addParameters(inputSize);
        this.isModelQuantized = isQuantized;
        this.addParameters(isModelQuantized);
        // Pre-allocate buffers.
        int numBytesPerChannel;
        if (isQuantized) {
            numBytesPerChannel = 1; // Quantized
            this.addParameters(numBytesPerChannel);
        } else {
            numBytesPerChannel = 4; // Floating point
            this.addParameters(numBytesPerChannel);
        }
        this.imgData = ByteBuffer.allocateDirect(this.inputSize * this.inputSize * 3 * numBytesPerChannel);
        this.imgData.order(ByteOrder.nativeOrder());
        this.addParameters(imgData);
        this.intValues = new int[this.inputSize * this.inputSize];
        this.addParameters(intValues);
    }


    @Override
    protected Object[] infer(UQI uqi, Item item){
        // Preprocess the image data from 0-255 int to normalized float based
        // on the provided parameters.

        bitmap = item.getValueByField(inputField);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, inputSize, inputSize);

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        imgData.rewind();
        for (int i = 0; i < inputSize; ++i) {
            for (int j = 0; j < inputSize; ++j) {
                int pixelValue = intValues[i * inputSize + j];
                if (isModelQuantized) {
                    // Quantized model
                    imgData.put((byte) ((pixelValue >> 16) & 0xFF));
                    imgData.put((byte) ((pixelValue >> 8) & 0xFF));
                    imgData.put((byte) (pixelValue & 0xFF));
                } else { // Float model
                    imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                }
            }
        }

;       Object[] inputArray = {imgData};

        return inputArray;
    }
}
