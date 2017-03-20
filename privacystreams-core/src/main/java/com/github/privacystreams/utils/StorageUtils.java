package com.github.privacystreams.utils;

import android.content.Context;
import android.os.Environment;

import com.github.privacystreams.core.UQI;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * A helper class to access storage-related functions.
 */

public class StorageUtils {
    private static final String LOG_TAG = "DropboxUtils - ";

    /**
     * Get the directory for the public directory.
     *
     * @param dirPath the path of dir
     * @return the directory in public dir
     */
    public static File getPublicDir(String dirPath) {
        String fullDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dirPath;
        File targetDir = new File(fullDirPath);
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            Logging.warn(LOG_TAG + "fail to create dir: " + targetDir);
            return null;
        }
        return targetDir;
    }

    /**
     * Get the relative path of a given file from the public directory.
     *
     * @param file the file to get path
     * @return the relative path
     */
    public static String getPublicRelativePath(File file) {
        File publicDir = Environment.getExternalStorageDirectory();
        return StringUtils.removeStart(file.getAbsolutePath(), publicDir.getAbsolutePath());
    }

    /**
     * Get the relative path of a given file from the private directory.
     *
     * @param file the file to get path
     * @return the relative path
     */
    public static String getPrivateRelativePath(Context context, File file) {
        File privateDir = context.getFilesDir();
        return StringUtils.removeStart(file.getAbsolutePath(), privateDir.getAbsolutePath());
    }

    /**
     * Get the directory for the user's private directory.
     *
     * @param dirPath the path of dir
     * @return the directory in private dir
     */
    public static File getPrivateDir(Context context, String dirPath) {
        String fullDirPath = context.getFilesDir().getAbsolutePath() + "/" + dirPath;
        File targetDir = new File(fullDirPath);
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            Logging.warn(LOG_TAG + "fail to create dir: " + targetDir);
            return null;
        }
        return targetDir;
    }

    /**
     * Get a valid file of a given file path.
     *
     * @param context a Context instance
     * @param filePath the original file path
     * @param isPublic if true, the valid path will be in external storage (sdcard);
     *                 if false, the valid path will be in internal storage.
     * @return the valid file
     */
    public static File getValidFile(Context context, String filePath, boolean isPublic) {
        String dirPath, fileName;
        int lastPathSepIndex = filePath.lastIndexOf('/');
        if (lastPathSepIndex < 0) {
            dirPath = "";
            fileName = filePath;
        }
        else {
            dirPath = filePath.substring(0, lastPathSepIndex);
            fileName = filePath.substring(lastPathSepIndex + 1);
        }

        File dirFile;
        if (isPublic) {
            dirFile = getPublicDir(dirPath);
        }
        else {
            dirFile = getPrivateDir(context, dirPath);
        }

        return new File(dirFile, fileName);
    }

}
