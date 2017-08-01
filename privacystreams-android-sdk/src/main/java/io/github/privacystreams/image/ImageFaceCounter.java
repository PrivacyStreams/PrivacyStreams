package io.github.privacystreams.image;

import io.github.privacystreams.core.UQI;

/**
 * Detect face in an image.
 */
class ImageFaceCounter extends ImageProcessor<Integer> {

    ImageFaceCounter(String imageDataField) {
        super(imageDataField);
    }

    @Override
    protected Integer processImage(UQI uqi, ImageData imageData) {
        return imageData.countFaces(uqi);
    }

}
