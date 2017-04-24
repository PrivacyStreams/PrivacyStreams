package com.github.privacystreams.storage;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * Dropbox service integration
 */
@PSOperatorWrapper
public class DropboxOperators {

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
    public static <Tin> Function<Tin, Void> uploadTo(final String filePath, boolean append) {
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
    public static <Tin> Function<Tin, Void> uploadTo(final Function<Tin, String> filePathGenerator, boolean append) {
        return new PSDropboxUploader<>(filePathGenerator, append);
    }
}
