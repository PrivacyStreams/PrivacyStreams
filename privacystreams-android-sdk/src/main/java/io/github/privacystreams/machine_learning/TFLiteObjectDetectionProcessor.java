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

class TFLiteObjectDetectionProcessor extends MLProcessor<Object[]>{
    Bitmap bitmap;
    String inputField;
    ByteBuffer imgData;
    int inputSize;
    int[] intValues;
    boolean isModelQuantized;
    private static final float IMAGE_MEAN = 128.0f;
    private static final float IMAGE_STD = 128.0f;
    Integer sensorOrientation;


    TFLiteObjectDetectionProcessor(String inputField, int inputSize, boolean isQuantized, Integer sensorOrientation){
        super(Arrays.asList(new String[]{inputField}));
        this.inputField = inputField;
        this.addParameters(inputField);
        this.sensorOrientation = sensorOrientation;
        this.addParameters(sensorOrientation);
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


        Matrix frameToCropTransform =
                getTransformationMatrix(
                        bitmap.getWidth(), bitmap.getHeight(),
                        inputSize, inputSize,
                        sensorOrientation, false);
        Bitmap croppedBitmap =  Bitmap.createBitmap(inputSize, inputSize, Bitmap.Config.ARGB_8888);

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(bitmap, frameToCropTransform, null);


        croppedBitmap.getPixels(intValues, 0, croppedBitmap.getWidth(), 0, 0,
                croppedBitmap.getWidth(), croppedBitmap.getHeight());

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
    public static Matrix getTransformationMatrix(
            final int srcWidth,
            final int srcHeight,
            final int dstWidth,
            final int dstHeight,
            final int applyRotation,
            final boolean maintainAspectRatio) {
        final Matrix matrix = new Matrix();

        if (applyRotation != 0) {
            if (applyRotation % 90 != 0) {

            }

            // Translate so center of image is at origin.
            matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f);

            // Rotate around origin.
            matrix.postRotate(applyRotation);
        }
        // Account for the already applied rotation, if any, and then determine how
        // much scaling is needed for each axis.
        final boolean transpose = (Math.abs(applyRotation) + 90) % 180 == 0;

        final int inWidth = transpose ? srcHeight : srcWidth;
        final int inHeight = transpose ? srcWidth : srcHeight;

        // Apply scaling if necessary.
        if (inWidth != dstWidth || inHeight != dstHeight) {
            final float scaleFactorX = dstWidth / (float) inWidth;
            final float scaleFactorY = dstHeight / (float) inHeight;

            if (maintainAspectRatio) {
                // Scale by minimum factor so that dst is filled completely while
                // maintaining the aspect ratio. Some image may fall off the edge.
                final float scaleFactor = Math.max(scaleFactorX, scaleFactorY);
                matrix.postScale(scaleFactor, scaleFactor);
            } else {
                // Scale exactly to fill dst from src.
                matrix.postScale(scaleFactorX, scaleFactorY);
            }
        }

        if (applyRotation != 0) {
            // Translate back from origin centered reference to destination frame.
            matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f);
        }

        return matrix;
    }
}
