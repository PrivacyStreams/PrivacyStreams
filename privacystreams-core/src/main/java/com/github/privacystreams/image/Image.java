package com.github.privacystreams.image;

import android.net.Uri;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.providers.SingleItemStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * An Image item represents an image file.
 */
@PSItem
public class Image extends Item {

    /**
     * The timestamp of when the image is generated.
     */
    @PSItemField(type = Long.class)
    private static final String TIMESTAMP = "timestamp";

    /**
     * The URI of image file.
     */
    @PSItemField(type = String.class)
    public static final String URI = "uri";

    // TODO create a metadata field, put lat and lng to metadata
    private static final String LAT = "lat";
    private static final String LNG = "lng";

    Image(String timestamp, Uri file_uri, double lat, double lng) {
        this.setFieldValue(TIMESTAMP, timestamp);
        this.setFieldValue(URI, file_uri.toString());
        this.setFieldValue(LAT,lat);
        this.setFieldValue(LNG,lng);
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
        return new ImageStorageProvider();
    }

}
