package com.github.privacystreams.image;

import com.github.privacystreams.core.UQI;

/**
 * Get the file path of the image specified by an ImageData field.
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
