package com.github.privacystreams.storage;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 30/12/2016.
 * A helper class to access print-related functions
 */

public class Storage {
    /**
     * A function that writes an item to file
     */
    public static Function<Item, Void> writeToFile(String dirPath, String fileTag) {
        return new ItemFileWriter(dirPath, fileTag);
    }
}
