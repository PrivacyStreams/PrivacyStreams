package com.github.privacystreams.image;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.core.UQI;

/**
 * Blur an image and return the blurred image data.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
class ImageBlurFunction extends ImageProcessor<ImageData> {

    ImageBlurFunction(String imageDataField) {
        super(imageDataField);
    }

    @Override
    protected ImageData processImage(UQI uqi, ImageData imageData) {
        return imageData.getBlurred(uqi);
    }

}
