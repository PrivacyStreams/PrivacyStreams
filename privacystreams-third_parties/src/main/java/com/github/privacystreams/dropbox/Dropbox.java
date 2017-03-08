package com.github.privacystreams.dropbox;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 20/02/2017.
 * Dropbox service integration
 */

public class Dropbox {
    /**
     * A function that upload an item to Dropbox
     */
    public static Function<Item, Void> upload(String DropboxToken, String fileTag) {
        return new ItemDropboxUploader(DropboxToken, fileTag);
    }
}
