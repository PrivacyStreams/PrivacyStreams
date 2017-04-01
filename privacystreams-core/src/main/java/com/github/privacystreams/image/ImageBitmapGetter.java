package com.github.privacystreams.image;

import android.graphics.Bitmap;
import android.media.ExifInterface;

import com.github.privacystreams.core.UQI;

/**
 * Retrieve the Bitmap from the photo specified by an ImageData field.
 */
class ImageBitmapGetter extends ImageProcessor<Bitmap> {

    ImageBitmapGetter(String photoField) {
        super(photoField);
    }

    @Override
    protected Bitmap processImage(UQI uqi, ImageData imageData) {
        return imageData.getBitmap(uqi);
    }

}
