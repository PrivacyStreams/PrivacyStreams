package com.github.privacystreams.image;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.core.UQI;

/**
 * Created by yuanchun on 28/12/2016.
 * Blur an image and return the blurred image data.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
class ImageBlurFunction extends ImageProcessor<ImageData> {

    ImageBlurFunction(String photoField) {
        super(photoField);
    }

    @Override
    protected ImageData processImage(UQI uqi, ImageData imageData) {
        return imageData.getBlurred(uqi.getContext());
    }

}
