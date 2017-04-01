package com.github.privacystreams.image;

import android.media.ExifInterface;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.location.LatLng;

/**
 * Retrieve the latitude and longitude of the image.
 */
class ImageLatLngRetriever extends ImageProcessor<LatLng> {

    ImageLatLngRetriever(String photoField) {
        super(photoField);
    }

    @Override
    protected LatLng processImage(UQI uqi, ImageData imageData) {
        return imageData.getLatLng(uqi);
    }

}
