package com.github.privacystreams.image;

import android.graphics.Bitmap;

import com.github.privacystreams.core.UQI;

/**
 * Retrieve the EXIF metadata of photo.
 * The EXIF information is an ExifInterface in Android.
 */
class ImageFilepathGetter extends ImageProcessor<String> {

    ImageFilepathGetter(String photoField) {
        super(photoField);
    }

    @Override
    protected String processImage(UQI uqi, ImageData imageData) {
        return imageData.getFilepath(uqi);
    }

}
