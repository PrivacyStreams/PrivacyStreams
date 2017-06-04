package com.github.privacystreams.image;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.location.LatLon;

/**
 * Detect face in an image.
 */
class ImageFaceDetector extends ImageProcessor<Boolean> {

    ImageFaceDetector(String imageDataField) {
        super(imageDataField);
    }

    @Override
    protected Boolean processImage(UQI uqi, ImageData imageData) {
        return imageData.hasFace(uqi);
    }

}
