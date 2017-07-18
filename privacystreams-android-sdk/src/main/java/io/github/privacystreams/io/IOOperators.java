package io.github.privacystreams.io;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access storage-related operators
 */
@PSOperatorWrapper
public class IOOperators {

    /**
     * Write an object to a local file, the output file will be at `filePath`.
     * If `isPublic` is true, the file will be created in `/sdcard/` folder, which can be accessed by other apps.
     * If `isPublic` is false, the file will be created in the app data folder, which can not be accessed by other apps;
     * If `append` is true, the object will be appended to the file;
     * If `append` is false, the object will overwrite the file.
     * This provider requires `android.permission.WRITE_EXTERNAL_STORAGE` permission.
     *
     * @param filePath the output file path
     * @param isPublic whether the file is public. If set to true, will require WRITE_EXTERNAL_STORAGE permission.
     * @param append whether the object will be appended to the file
     * @param <Tin> the type of input object
     * @return the function
     */
    // @RequiresPermission(value = Manifest.permission.WRITE_EXTERNAL_STORAGE, conditional = true)
    public static <Tin> Function<Tin, Void> writeToFile(final String filePath, boolean isPublic, boolean append) {
        return new PSFileWriter<>(new Function<Tin, String>() {
            @Override
            public String apply(UQI uqi, Tin input) {
                return filePath;
            }
        }, isPublic, append);
    }

    /**
     * Write an object to a local file, the output file path will be generated with a function.
     * If `isPublic` is true, the file will be created in `/sdcard/` folder, which can be accessed by other apps.
     * If `isPublic` is false, the file will be created in the app data folder, which can not be accessed by other apps;
     * If `append` is true, the object will be appended to the file;
     * If `append` is false, the object will overwrite the file.
     * This provider requires `android.permission.WRITE_EXTERNAL_STORAGE` permission.
     *
     * @param filePathGenerator the function to generate the output file path each time
     * @param isPublic whether the file is public. If set to true, will requires WRITE_EXTERNAL_STORAGE permission.
     * @param append whether the object will be appended to the file
     * @param <Tin> the type of input object
     * @return the function
     */
    // @RequiresPermission(value = Manifest.permission.WRITE_EXTERNAL_STORAGE, conditional = true)
    public static <Tin> Function<Tin, Void> writeToFile(final Function<Tin, String> filePathGenerator, boolean isPublic, boolean append) {
        return new PSFileWriter<>(filePathGenerator, isPublic, append);
    }

    /**
     * Upload an object to Dropbox, the output file will be at `filePath`.
     * If there is a file already at the `filePath`, the item will be appended to the file.
     * This operator requires Dropbox configured (see https://privacystreams.github.io/pages/enable_accessibility.html).
     * This provider requires `android.permission.INTERNET` permission.
     *
     * @param filePath the output file path
     * @param <Tin> the type of input object
     * @return the function
     */
    // @RequiresPermission(value = Manifest.permission.INTERNET)
    public static <Tin> Function<Tin, Void> uploadToDropbox(final String filePath, boolean append) {
        return new PSDropboxUploader<>(new Function<Tin, String>() {
            @Override
            public String apply(UQI uqi, Tin input) {
                return filePath;
            }
        }, append);
    }

    /**
     * Upload an object to Dropbox, the output file path will be generated with a function.
     * If file already exists and `append` is true, the object will be appended to the file;
     * If `append` is false, the object will overwrite the existing file.
     * This operator requires Dropbox configured (see https://privacystreams.github.io/pages/enable_accessibility.html).
     * This provider requires `android.permission.INTERNET` permission.
     *
     * @param filePathGenerator the function to generate the output file path each time
     * @param <Tin> the type of input object
     * @return the function
     */
    // @RequiresPermission(value = Manifest.permission.INTERNET)
    public static <Tin> Function<Tin, Void> uploadToDropbox(final Function<Tin, String> filePathGenerator, boolean append) {
        return new PSDropboxUploader<>(filePathGenerator, append);
    }

}
