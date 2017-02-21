package com.github.privacystreams.image;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanchun on 20/02/2017.
 * Image class
 */

public class Image extends Item {

    // type: Long
    public static final String TIMESTAMP = "timestamp";
    // type: String, representing the URI of photo file
    public static final String URI = "uri";

    Image(String timestamp, java.net.URI file_uri) {
        this.setFieldValue(TIMESTAMP, timestamp);
        this.setFieldValue(URI, file_uri.toString());
    }

    /**
     * get a item provider that takes a photo using camera
     * the photo item represents the taken photo
     * @return the provider
     */
    public static SingleItemStreamProvider takeFromCamera() {
        // TODO implement this
        return null;
    }

    /**
     * get a stream provider that provides the photo items from file system
     * each photo item represents a photo in storage
     * @return the provider
     */
    public static MultiItemStreamProvider readFromStorage() {
        // TODO implement this
        return null;
    }

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
