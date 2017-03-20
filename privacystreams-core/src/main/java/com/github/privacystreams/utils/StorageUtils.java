package com.github.privacystreams.utils;

import android.content.Context;
import android.os.Environment;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A helper class to access storage-related functions.
 */

public class StorageUtils {
    private static final String LOG_TAG = "DropboxUtils - ";

    public static final Object fileRWMutex = new Object();

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

    public static void writeToFile(String content, File validFile, boolean append) {
        try {
            synchronized (fileRWMutex) {
                String contentToWrite = append ? GlobalConfig.StorageConfig.fileAppendSeparator + content : content;
                FileOutputStream fileOutputStream = new FileOutputStream(validFile, append);
                fileOutputStream.write(contentToWrite.getBytes());
                fileOutputStream.close();
            }
        } catch (IOException e) {
            Logging.warn("error writing data to file.");
            e.printStackTrace();
        }
    }

    /**
     * Get the input stream of a file and delete the file.
     *
     * @param file the file to read
     * @return the InputStream
     */
    public static InputStream getInputStreamAndDelete(File file) {
        ByteArrayOutputStream tempOutStream = new ByteArrayOutputStream();
        InputStream resultInputStream = null;

        try {
            synchronized (StorageUtils.fileRWMutex) {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) > -1) {
                    tempOutStream.write(buffer, 0, len);
                }
                tempOutStream.flush();
                inputStream.close();
                file.delete();
                resultInputStream = new ByteArrayInputStream(tempOutStream.toByteArray());
            }
        } catch (IOException e) {
            Logging.warn("error getting data from file.");
            e.printStackTrace();
        }

        return resultInputStream;
    }

}
