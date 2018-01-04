package io.github.privacystreams.image;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

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
     * This field is only available with `readStorage` provider.
     */
    @PSItemField(type = Integer.class)
    public static final String BUCKET_ID = "bucket_id";

    /**
     * The name of the bucket (folder) that the image belongs to.
     * This field is only available with `readStorage` provider.
     */
    @PSItemField(type = String.class)
    public static final String BUCKET_NAME = "bucket_name";

    /**
     * The id of the image in Android media database.
     * This field is only available with `readStorage` provider.
     */
    @PSItemField(type = Integer.class)
    public static final String IMAGE_ID = "image_id";

    /**
     * The name of the image.
     * This field is only available with `readStorage` provider.
     */
    @PSItemField(type = String.class)
    public static final String IMAGE_NAME = "image_name";

    /**
     * The file path of the image.
     * This field is only available with `readStorage` provider.
     */
    @PSItemField(type = String.class)
    public static final String IMAGE_PATH = "image_path";

    Image(Long dateAdded, ImageData imageData) {
        this.setFieldValue(DATE_ADDED, dateAdded);
        this.setFieldValue(IMAGE_DATA, imageData);
    }

    /**
     * Provide an PStream with an Image item, which represents a photo taken using camera.
     * The user will be redirected to the camera app to take a photo.
     * This provider requires `android.permission.CAMERA` permission
     * and `android.permission.WRITE_EXTERNAL_STORAGE` permission.
     *
     * @return the provider function.
     */
//    @RequiresPermission(allOf = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static PStreamProvider takePhoto() {
        return new ImageCameraProvider();
    }

    /**
     * Provide an PStream with an Image item, which is a photo taken using camera in background.
     * This provider requires `android.permission.CAMERA`, `android.permission.SYSTEM_ALERT_WINDOW`,
     * and overlay permission.
     *
     * @param cameraId the camera id. Usually 0 is the rear camera and 1 is the front.
     * @return the provider function.
     */
//    @RequiresPermission(allOf = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static PStreamProvider takePhotoBg(int cameraId) {
        return new BgPhotoProvider(cameraId);
    }

    /**
     * Provide an PStream with Image items, which are photos taken using camera in background.
     * This provider requires `android.permission.CAMERA`, `android.permission.SYSTEM_ALERT_WINDOW`,
     * and overlay permission.
     *
     * @param cameraId the camera id. Usually 0 is the rear camera and 1 is the front.
     * @param interval the interval between each two photos (in milliseconds).
     * @return the provider function.
     */
//    @RequiresPermission(allOf = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static PStreamProvider takePhotoBgPeriodic(int cameraId, long interval) {
        return new BgPhotoPeriodicProvider(cameraId, interval);
    }

    /**
     * Provide a stream of all Image items in local file system.
     * This provider requires `android.permission.READ_EXTERNAL_STORAGE` permission.
     *
     * @return the provider function.
     */
    // @RequiresPermission(value = Manifest.permission.READ_EXTERNAL_STORAGE)
    public static PStreamProvider readStorage() {
        return new ImageStorageProvider();
    }

}
