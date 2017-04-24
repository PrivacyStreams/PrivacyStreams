package com.github.privacystreams.image;

import android.Manifest;
import android.support.annotation.RequiresPermission;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.providers.SStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * An Image item represents an image, could be an image file from storage, etc.
 */
@PSItem
public class Image extends Item {

    /**
     * The timestamp of when the Image item was generated.
     */
    @PSItemField(type = Long.class)
    public static final String DATE_ADDED = "date_added";

    /**
     * The abstraction of image data.
     * The value is an `ImageData` instance.
     */
    @PSItemField(type = ImageData.class)
    public static final String IMAGE_DATA = "image_data";

    /**
     * The id of the bucket (folder) that the image belongs to.
     * This field is only available with `getFromStorage` provider.
     */
    @PSItemField(type = Integer.class)
    public static final String BUCKET_ID = "bucket_id";

    /**
     * The name of the bucket (folder) that the image belongs to.
     * This field is only available with `getFromStorage` provider.
     */
    @PSItemField(type = String.class)
    public static final String BUCKET_NAME = "bucket_name";

    /**
     * The id of the image in Android media database.
     * This field is only available with `getFromStorage` provider.
     */
    @PSItemField(type = Integer.class)
    public static final String IMAGE_ID = "image_id";

    /**
     * The name of the image.
     * This field is only available with `getFromStorage` provider.
     */
    @PSItemField(type = String.class)
    public static final String IMAGE_NAME = "image_name";

    /**
     * The file path of the image.
     * This field is only available with `getFromStorage` provider.
     */
    @PSItemField(type = String.class)
    public static final String IMAGE_PATH = "image_path";

    Image(Long dateAdded, ImageData imageData) {
        this.setFieldValue(DATE_ADDED, dateAdded);
        this.setFieldValue(IMAGE_DATA, imageData);
    }

    /**
     * Provide an SStream with an Image item, which represents a photo taken from camera.
     * This provider requires `android.permission.CAMERA` permission
     * and `android.permission.WRITE_EXTERNAL_STORAGE` permission.
     *
     * @return the provider function.
     */
//    @RequiresPermission(allOf = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static SStreamProvider takeFromCamera() {
        return new ImageCameraProvider();
    }

    /**
     * Provide a stream of all Image items in local file system.
     * This provider requires `android.permission.READ_EXTERNAL_STORAGE` permission.
     *
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.READ_EXTERNAL_STORAGE)
    public static MStreamProvider getFromStorage() {
        return new ImageStorageProvider();
    }

}
