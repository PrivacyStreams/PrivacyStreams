package com.github.privacystreams.image;

import android.Manifest;
import android.support.annotation.RequiresPermission;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * An Image item represents an image, could be an image file from storage, etc.
 */
@PSItem
public class Image extends Item {

    /**
     * The timestamp of when the image was generated.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * The abstraction of image data.
     * The value is an `ImageData` instance.
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
    // @RequiresPermission(value = Manifest.permission.READ_EXTERNAL_STORAGE)
    public static MStreamProvider getFromStorage() {
        return new ImageStorageProvider();
    }

}
