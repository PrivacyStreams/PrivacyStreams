package com.github.privacystreams.storage;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.List;

/**
 * Dropbox service integration
 */
@PSOperatorWrapper
public class DropboxOperators {
    /**
     * Upload an object to Dropbox.
     * This operator requires Dropbox configured, including:
     * 1. Adding `'com.dropbox.core:dropbox-core-sdk:2.1.1'` in build.gradle dependencies;
     * 2. Creating an Dropbox app and getting an access token of the app.
     * For more details, please refer to [Dropbox developers page](https://www.dropbox.com/developers/apps/).
     * The uploaded object will be at `<Dropbox_app>/<uuid>/<fileTag>_<timeTag>.json` in your Dropbox.
     *
     * @param fileTag the file tag of uploaded files
     * @return the function
     */
    public static <Tin> Function<Tin, Void> uploadAs(String fileTag) {
        return new DropboxUploader<>(fileTag);
    }

    /**
     * Append an object to Dropbox.
     * This operator requires Dropbox configured, including:
     * 1. Adding `'com.dropbox.core:dropbox-core-sdk:2.1.1'` in build.gradle dependencies;
     * 2. Creating an Dropbox app and getting an access token of the app.
     * For more details, please refer to [Dropbox developers page](https://www.dropbox.com/developers/apps/).
     * The uploaded object will be at `<Dropbox_app>/<uuid>/<fileName>` in your Dropbox.
     *
     * @param fileName the name of the Dropbox file to append
     * @return the function
     */
    public static <Tin> Function<Tin, Void> appendTo(String fileName) {
        return new DropboxAppender<>(fileName);
    }
}
