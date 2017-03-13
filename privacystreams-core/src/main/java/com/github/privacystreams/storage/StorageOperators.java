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
     * Output an item to file system, the output file name will be `<tag><timestamp>.json`.
     *
     * @param dirPath the directory to output the item.
     * @param fileTag the tag of the output file.
     * @return the function.
     */
    public static Function<Item, Void> writeToFile(String dirPath, String fileTag) {
        return new ExternalStorageWriter(dirPath, fileTag);
    }
}
