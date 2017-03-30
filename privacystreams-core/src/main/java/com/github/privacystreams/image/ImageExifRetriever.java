package com.github.privacystreams.image;

import android.media.ExifInterface;
import android.net.Uri;

import com.github.privacystreams.core.UQI;

import java.util.Map;

/**
 * Retrieve the EXIF metadata of photo.
 * The EXIF information is an ExifInterface in Android.
 */
class ImageExifRetriever extends ImageProcessor<ExifInterface> {

    ImageExifRetriever(String photoField) {
        super(photoField);
    }

    @Override
    protected ExifInterface processImage(UQI uqi, ImageData imageData) {
        return imageData.getExif();
    }

}
