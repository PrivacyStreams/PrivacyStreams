package io.github.privacystreams.image;

import io.github.privacystreams.core.UQI;

/**
 * Detect face in an image.
 */
class ImageTextExtractor extends ImageProcessor<String> {

    ImageTextExtractor(String imageDataField) {
        super(imageDataField);
    }

    @Override
    protected String processImage(UQI uqi, ImageData imageData) {
        return imageData.detectText(uqi);
    }

}
