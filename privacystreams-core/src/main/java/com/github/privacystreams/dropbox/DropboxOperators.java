package com.github.privacystreams.dropbox;

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
     * A function that upload an item to Dropbox.
     * This operator requires Dropbox configured, including:
     * 1. Adding `'com.dropbox.core:dropbox-core-sdk:2.1.1'` in build.gradle dependencies;
     * 2. Adding `"com.dropbox.core.android.AuthActivity"` to manifest like [this](https://github.com/dropbox/dropbox-sdk-java/blob/master/examples/android/src/main/AndroidManifest.xml).
     * For more details, please refer to [Dropbox documentation](https://www.dropbox.com/developers).
     *
     * @param DropboxToken the Dropbox token, can get from [Dropbox developers page](https://www.dropbox.com/developers/apps/)
     * @param fileTag the file tag of uploaded files
     * @return the function
     */
    public static Function<Item, Void> upload(String DropboxToken, String fileTag) {
        return new ItemDropboxUploader(DropboxToken, fileTag);
    }

    /**
     * A function that upload all items to Dropbox.
     * This operator requires Dropbox configured, please refer to {@link #upload(String, String)} for how to configure.
     *
     * @param DropboxToken the Dropbox token, can get from [Dropbox developers page](https://www.dropbox.com/developers/apps/)
     * @param fileTag the file tag of uploaded files
     * @return the function
     */
    public static Function<List<Item>, Void> uploadAll(String DropboxToken, String fileTag) {
        return new ItemsDropboxUploader(DropboxToken, fileTag);
    }
}
