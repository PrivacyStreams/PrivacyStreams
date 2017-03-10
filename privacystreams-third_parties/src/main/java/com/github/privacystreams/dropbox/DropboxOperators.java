package com.github.privacystreams.dropbox;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 20/02/2017.
 * Dropbox service integration
 */

public class DropboxOperators {
    /**
     * A function that upload an item to Dropbox
     * @param DropboxToken the Dropbox token, get one from https://www.dropbox.com/developers/apps/
     * @param fileTag the file tag of uploaded files
     * @return the function
     */
    public static Function<Item, Void> upload(String DropboxToken, String fileTag) {
        return new ItemDropboxUploader(DropboxToken, fileTag);
    }
}
