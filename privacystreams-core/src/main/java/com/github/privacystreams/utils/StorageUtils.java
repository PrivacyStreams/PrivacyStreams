package com.github.privacystreams.utils;

import android.os.Environment;

import java.io.File;

/**
 * A helper class to access storage-related functions.
 */

public class StorageUtils {
    public static File getPublicDir(String dirName) {
        // Get the directory for the user's public directory.
        File targetDir = new File(Environment.getExternalStorageDirectory(), dirName);
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            Logging.warn("fail to create dir: " + targetDir);
            return null;
        }
        return targetDir;
    }
}
