package com.github.privacystreams.image;

import android.net.Uri;

/**
 * Created by yuanchun on 28/12/2016.
 * A function that blurs a photo and returns the blurred photo uri string.
 */
class ImageBlurFunction extends ImageProcessor<String> {

    ImageBlurFunction(String photoField) {
        super(photoField);
    }

    @Override
    protected String processPhoto(Uri photoUri) {
        Uri blurredPhotoUri = this.blurPhoto(photoUri);
        if (blurredPhotoUri == null) return null;
        return blurredPhotoUri.toString();
    }

    private Uri blurPhoto(Uri originalPhotoUri) {
        // TODO blur the photo and return the uri of blurred photo
        return null;
    }

}
