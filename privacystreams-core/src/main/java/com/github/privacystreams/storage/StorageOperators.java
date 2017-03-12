package com.github.privacystreams.storage;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access storage-related operators
 */
@PSOperatorWrapper
public class StorageOperators {
    /**
     * A function that writes an item to file
     */
    public static Function<Item, Void> writeToFile(String dirPath, String fileTag) {
        return new ExternalStorageWriter(dirPath, fileTag);
    }
}
