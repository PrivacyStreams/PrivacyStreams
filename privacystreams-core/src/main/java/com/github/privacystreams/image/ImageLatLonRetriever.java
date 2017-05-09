package com.github.privacystreams.image;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.location.LatLon;

/**
 * Retrieve the latitude and longitude of the image.
 */
class ImageLatLonRetriever extends ImageProcessor<LatLon> {

    ImageLatLonRetriever(String imageDataField) {
        super(imageDataField);
    }

    @Override
    protected LatLon processImage(UQI uqi, ImageData imageData) {
        return imageData.getLatLon(uqi);
    }

}
