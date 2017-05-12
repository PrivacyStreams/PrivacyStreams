package com.github.privacystreams.image;

import com.github.privacystreams.core.UQI;

/**
 * Detect face in an image.
 */
class ImageCharacterDetector extends ImageProcessor<Boolean> {

    ImageCharacterDetector(String imageDataField) {
        super(imageDataField);
    }

    @Override
    protected Boolean processImage(UQI uqi, ImageData imageData) {
        return imageData.hasCharacter(uqi);
    }

}
