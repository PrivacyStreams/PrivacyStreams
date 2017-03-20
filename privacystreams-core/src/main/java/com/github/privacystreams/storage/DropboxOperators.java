package com.github.privacystreams.storage;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.List;

/**
 * Dropbox service integration
 */
@PSOperatorWrapper
public class DropboxOperators {
//    /**
//     * Upload an object to Dropbox.
//     * This operator requires Dropbox configured, including:
//     * 1. Adding `'com.dropbox.core:dropbox-core-sdk:2.1.1'` in build.gradle dependencies;
//     * 2. Creating an Dropbox app and getting an access token of the app.
//     * For more details, please refer to [Dropbox developers page](https://www.dropbox.com/developers/apps/).
//     * The uploaded object will be at `<Dropbox_app>/<uuid>/<fileTag>_<timeTag>.json` in your Dropbox.
//     *
//     * @param fileTag the file tag of uploaded files
//     * @return the function
//     */
//    public static <Tin> Function<Tin, Void> uploadAs(String fileTag) {
//        return new DropboxUploader<>(fileTag);
//    }

//    /**
//     * Append an object to Dropbox.
//     * This operator requires Dropbox configured, including:
//     * 1. Adding `'com.dropbox.core:dropbox-core-sdk:2.1.1'` in build.gradle dependencies;
//     * 2. Creating an Dropbox app and getting an access token of the app.
//     * For more details, please refer to [Dropbox developers page](https://www.dropbox.com/developers/apps/).
//     * The uploaded object will be at `<Dropbox_app>/<uuid>/<fileName>` in your Dropbox.
//     *
//     * @param fileName the name of the Dropbox file to append
//     * @return the function
//     */
//    public static <Tin> Function<Tin, Void> appendTo(String fileName) {
//        return new DropboxAppender<>(fileName);
//    }

    /**
     * Upload an object to Dropbox, the output file will be at `filePath`.
     * If there is a file already at the `filePath`, the item will be appended to the file.
     *
     * This operator requires Dropbox configured, please set proper values in `GlobalConfig.DropboxConfig`.
     *
     * @param filePath the output file path
     * @param <Tin> the type of input object
     * @return the function
     */
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
     *
     * @param filePathGenerator the function to generate the output file path each time
     * @param <Tin> the type of input object
     * @return the function
     */
    public static <Tin> Function<Tin, Void> uploadTo(final Function<Tin, String> filePathGenerator, boolean append) {
        return new PSDropboxUploader<>(filePathGenerator, append);
    }
}
