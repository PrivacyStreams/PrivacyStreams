package com.github.privacystreams.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.github.privacystreams.core.UQI;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A helper class to access Dropbox-related utility functions.
 */

public class DropboxUtils {
    private static final String LOG_TAG = "DropboxUtils - ";

    private static final String DROPBOX_WAITING_LIST = "dropbox_waiting_list";

    private static long lastSyncTimestamp = 0;

    private static final Object dropboxWaitingListMutex = new Object();
    private static final Object dropboxUploadMutex = new Object();

    public static void addToWaitingList(UQI uqi, String fileName) {
        SharedPreferences pref = uqi.getContext().getApplicationContext().getSharedPreferences(Consts.LIB_TAG, Context.MODE_PRIVATE);
        Set<String> waitingList = pref.getStringSet(DROPBOX_WAITING_LIST, new HashSet<String>());
        if (waitingList.contains(fileName)) return;

        synchronized (dropboxWaitingListMutex) {
            waitingList.add(fileName);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.putStringSet(DROPBOX_WAITING_LIST, waitingList);
            editor.apply();
            Logging.debug(LOG_TAG + "Added file to waiting list: " + fileName);
        }
    }

    public static void removeFromWaitingList(UQI uqi, Set<String> fileNamesToRemove) {
        synchronized (dropboxWaitingListMutex) {
            SharedPreferences pref = uqi.getContext().getApplicationContext().getSharedPreferences(Consts.LIB_TAG, Context.MODE_PRIVATE);
            Set<String> waitingList = pref.getStringSet(DROPBOX_WAITING_LIST, new HashSet<String>());
            waitingList.removeAll(fileNamesToRemove);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.putStringSet(DROPBOX_WAITING_LIST, waitingList);
            editor.apply();
            Logging.debug(LOG_TAG + "Removed files from waiting list: " + fileNamesToRemove);
        }
    }

    private static boolean syncing = false;

    public static void syncFiles(UQI uqi, boolean append) {
        if (syncing) return;

        long currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp - lastSyncTimestamp < GlobalConfig.DropboxConfig.leastSyncInterval)
            return;

        synchronized (dropboxUploadMutex) {
            try {
                SharedPreferences pref = uqi.getContext().getApplicationContext().getSharedPreferences(Consts.LIB_TAG, Context.MODE_PRIVATE);
                Set<String> waitingList = pref.getStringSet(DROPBOX_WAITING_LIST, new HashSet<String>());

                if (waitingList.isEmpty()) return;
                if (GlobalConfig.DropboxConfig.onlyOverWifi && !ConnectionUtils.isWifiConnected(uqi))
                    return;

                syncing = true;
                // Create Dropbox client
                DbxRequestConfig config = new DbxRequestConfig(Consts.LIB_TAG);
                DbxClientV2 client = new DbxClientV2(config, GlobalConfig.DropboxConfig.accessToken);

                Set<File> filesToDelete = new HashSet<>();
                Set<String> filesToRemoveFromWaitingList = new HashSet<>();
                Set<String> filesToUpload = new HashSet<>(waitingList);

                Logging.debug(LOG_TAG + "Trying to upload: " + filesToUpload);

                for (String fileToUpload : filesToUpload) {
                    try {
                        File localFile = new File(fileToUpload);
                        String remotePath = StorageUtils.getPrivateRelativePath(uqi.getContext(), localFile);
                        FileInputStream inputStream = new FileInputStream(localFile);
                        client.files()
                                .uploadBuilder(remotePath)
                                .withMode(WriteMode.ADD)
                                .withAutorename(true)
                                .uploadAndFinish(inputStream);
                        inputStream.close();
                        filesToDelete.add(localFile);
                        filesToRemoveFromWaitingList.add(fileToUpload);
                    } catch (FileNotFoundException e) {
                        filesToRemoveFromWaitingList.add(fileToUpload);
                    }
                }

                Logging.debug(LOG_TAG + "Successfully uploaded: " + filesToUpload);

                removeFromWaitingList(uqi, filesToRemoveFromWaitingList);

                for (File fileToDelete : filesToDelete) {
                    fileToDelete.delete();
                }

                lastSyncTimestamp = System.currentTimeMillis();

            } catch (IOException | DbxException e) {
                Logging.warn("error uploading files to Dropbox.");
                e.printStackTrace();
            }
            syncing = false;
        }
    }

}
