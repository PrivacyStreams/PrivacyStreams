package com.github.privacystreams.image;

import android.graphics.Bitmap;

import com.github.privacystreams.core.UQI;

/**
 * Retrieve the Bitmap from the photo specified by an ImageData field.
 */
class ImageBitmapGetter extends ImageProcessor<Bitmap> {

    ImageBitmapGetter(String imageDataField) {
        super(imageDataField);
    }

    @Override
    protected Bitmap processImage(UQI uqi, ImageData imageData) {
        return imageData.getBitmap(uqi);
    }

}
