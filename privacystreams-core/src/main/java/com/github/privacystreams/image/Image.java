package com.github.privacystreams.image;

import android.net.Uri;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.providers.SStreamProvider;
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
    public static final String TIMESTAMP = "timestamp";

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
     * Provide an Image item, which represents a photo taken from camera.
     * @return the provider function.
     */
    public static SStreamProvider takeFromCamera() {
        // TODO implement this
        return null;
    }

    /**
     * Provide a list of Image items that are read from file system.
     *
     * @return the provider function.
     */
    public static MStreamProvider readFromStorage() {
        return new ImageStorageProvider();
    }

}
