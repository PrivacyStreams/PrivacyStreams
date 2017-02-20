package com.github.privacystreams.image;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

import java.util.Map;

/**
 * Created by yuanchun on 20/02/2017.
 * Image class
 */

public class Image extends Item {

    /**
     * A function that retrieves the metadata of the photo field in an item.
     * The metadata of a photo is a Map, in which each key-value pair represents a metadata item.
     * @param photoUriField the name of photo uri field
     * @return the function
     */
    public static Function<Item, Map<String, String>> getMetadata(String photoUriField) {
        return new ImageMetadataRetriever(photoUriField);
    }

    /**
     * A function that blurs the photo specified by the photo uri field in an item,
     * and returns the uri string of blurred photo.
     * @param photoUriField the name of photo uri field
     * @return the function
     */
    public static Function<Item, String> blur(String photoUriField) {
        return new ImageBlurFunction(photoUriField);
    }
}
