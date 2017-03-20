package com.github.privacystreams.storage;

import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access storage-related operators
 */
@PSOperatorWrapper
public class StorageOperators {
//    /**
//     * Output an object to a local file, the output file name will be `<fileTag>_<timeTag>.json`,
//     * for example, if `fileTag="location"`, the file name will be `location_20170315_170010_123.json`.
//     * If `dirPath` is null, the file will be created in the app data folder, which can not be accessed by other apps.
//     * If `dirPath` is not null, the file will be created in '/sdcard/<dirPath>/` folder, which can be accessed by other apps.
//     * The file will be created if it doesn't exist, and will be overwritten if it exists.
//     *
//     * @param dirPath the directory to output the object.
//     * @param fileTag the tag of the output file.
//     * @param <Tin> the type of input object, could be Item, Item list, or any object that is serializable.
//     * @return the function.
//     */
//    public static <Tin> Function<Tin, Void> writeTo(String dirPath, String fileTag) {
//        return new FileWriter<>(dirPath, fileTag);
//    }
//
//    /**
//     * Append an object to a local file, the output file name will be `<fileName>`,
//     * for example, if `fileName="locations.txt"`, the file name will be `locations.txt`.
//     * If `dirPath` is null, the file will be created in the app data folder, which can not be accessed by other apps.
//     * If `dirPath` is not null, the file will be created in '/sdcard/<dirPath>/` folder, which can be accessed by other apps.
//     * The file will be created if it doesn't exist.
//     * The appended objects will be separated by three lines (`\n\n\n`).
//     *
//     * @param dirPath the directory to output the object.
//     * @param fileName the name of the output file.
//     * @param <Tin> the type of input object, could be Item, Item list, or any object that is serializable.
//     * @return the function.
//     */
//    public static <Tin> Function<Tin, Void> appendTo(String dirPath, String fileName) {
//        return new FileWriter<>(dirPath, fileName);
//    }

    /**
     * Write an object to a local file, the output file will be at `filePath`.
     * If `isPublic` is true, the file will be created in '/sdcard/` folder, which can be accessed by other apps.
     * If `isPublic` is false, the file will be created in the app data folder, which can not be accessed by other apps;
     * If `append` is true, the object will be appended to the file;
     * If `append` is false, the object will overwrite the file.
     *
     * @param filePath the output file path
     * @param isPublic whether the file is public
     * @param append whether the object will be appended to the file
     * @param <Tin> the type of input object
     * @return the function
     */
    public static <Tin> Function<Tin, Void> writeTo(final String filePath, boolean isPublic, boolean append) {
        return new PSFileWriter<>(new Function<Tin, String>() {
            @Override
            public String apply(UQI uqi, Tin input) {
                return filePath;
            }
        }, isPublic, append);
    }

    /**
     * Write an object to a local file, the output file path will be generated with a function.
     * If `isPublic` is true, the file will be created in '/sdcard/` folder, which can be accessed by other apps.
     * If `isPublic` is false, the file will be created in the app data folder, which can not be accessed by other apps;
     * If `append` is true, the object will be appended to the file;
     * If `append` is false, the object will overwrite the file.
     *
     * @param filePathGenerator the function to generate the output file path each time
     * @param isPublic whether the file is public
     * @param append whether the object will be appended to the file
     * @param <Tin> the type of input object
     * @return the function
     */
    public static <Tin> Function<Tin, Void> writeTo(final Function<Tin, String> filePathGenerator, boolean isPublic, boolean append) {
        return new PSFileWriter<>(filePathGenerator, isPublic, append);
    }
}
