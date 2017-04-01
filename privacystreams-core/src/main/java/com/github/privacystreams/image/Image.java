package com.github.privacystreams.image;

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
    @PSItemField(type = ImageData.class)
    public static final String IMAGE_DATA = "image_data";

    Image(Long timestamp, ImageData imageData) {
        this.setFieldValue(TIMESTAMP, timestamp);
        this.setFieldValue(IMAGE_DATA, imageData);
    }

//    /**
//     * Provide an Image item, which represents a photo taken from camera.
//     *
//     * @return the provider function.
//     */
//    public static SStreamProvider takeFromCamera() {
//        return null;
//    }

    /**
     * Provide a stream of all Image items in local file system.
     *
     * @return the provider function.
     */
    public static MStreamProvider getFromStorage() {
        return new ImageStorageProvider();
    }

}
