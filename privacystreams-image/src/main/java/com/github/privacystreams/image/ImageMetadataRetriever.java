package com.github.privacystreams.image;

import android.net.Uri;

import java.util.Map;

/**
 * Created by yuanchun on 28/12/2016.
 * A function that retrieves the metadata of photo.
 * The metadata is a key-value map.
 */
class ImageMetadataRetriever extends ImageProcessor<Map<String, String>> {

    ImageMetadataRetriever(String photoField) {
        super(photoField);
    }

    @Override
    protected Map<String, String> processPhoto(Uri photoUri) {
        // TODO retrieve metadata from given photo uri.
        return null;
    }

}
