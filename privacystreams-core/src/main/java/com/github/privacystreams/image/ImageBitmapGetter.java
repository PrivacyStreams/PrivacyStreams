package com.github.privacystreams.image;

import android.graphics.Bitmap;
import android.media.ExifInterface;

import com.github.privacystreams.core.UQI;

/**
 * Retrieve the EXIF metadata of photo.
 * The EXIF information is an ExifInterface in Android.
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
