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

    public static synchronized void addToWaitingList(UQI uqi, String fileName) {
        SharedPreferences pref = uqi.getContext().getApplicationContext().getSharedPreferences(Consts.LIB_TAG, Context.MODE_PRIVATE);
        Set<String> waitingList = pref.getStringSet(DROPBOX_WAITING_LIST, new HashSet<String>());
        waitingList.add(fileName);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.putStringSet(DROPBOX_WAITING_LIST, waitingList);
        editor.apply();
        Logging.debug(LOG_TAG + "Added file to waiting list: " + fileName);
    }

    public static synchronized void syncFiles(UQI uqi) {
        long currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp - lastSyncTimestamp < GlobalConfig.DropboxConfig.leastSyncInterval)
            return;

        try {
            SharedPreferences pref = uqi.getContext().getApplicationContext().getSharedPreferences(Consts.LIB_TAG, Context.MODE_PRIVATE);
            Set<String> waitingList = pref.getStringSet(DROPBOX_WAITING_LIST, new HashSet<String>());

            if (waitingList.isEmpty()) return;
            if (GlobalConfig.DropboxConfig.onlyOverWifi && !ConnectionUtils.isWifiConnected(uqi)) return;

            // Create Dropbox client
            DbxRequestConfig config = new DbxRequestConfig(Consts.LIB_TAG);
            DbxClientV2 client = new DbxClientV2(config, GlobalConfig.DropboxConfig.accessToken);

            Set<String> filesToDelete = new HashSet<>();
            Set<String> filesToRemoveFromWaitingList = new HashSet<>();

            Logging.debug(LOG_TAG + "Trying to upload: " + waitingList);

            for (String fileToUpload : waitingList) {
                try {
                    FileInputStream inputStream = uqi.getContext().openFileInput(fileToUpload);
                    client.files()
                            .uploadBuilder("/" + uqi.getUUID() + "/" + fileToUpload)
                            .withMode(WriteMode.ADD)
                            .uploadAndFinish(inputStream);
                    inputStream.close();
                    filesToDelete.add(fileToUpload);
                    filesToRemoveFromWaitingList.add(fileToUpload);
                }
                catch (FileNotFoundException e) {
                    filesToRemoveFromWaitingList.add(fileToUpload);
                }
            }

            Logging.debug(LOG_TAG + "Successfully uploaded: " + waitingList);

            if (!filesToRemoveFromWaitingList.isEmpty()) {
                Set<String> newWaitingList = new HashSet<>(waitingList);
                newWaitingList.removeAll(filesToRemoveFromWaitingList);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.putStringSet(DROPBOX_WAITING_LIST, newWaitingList);
                editor.apply();
            }

            for (String fileToDelete : filesToDelete) {
                uqi.getContext().deleteFile(fileToDelete);
            }

            lastSyncTimestamp = System.currentTimeMillis();

            Logging.debug("Upload finished.");

        } catch (IOException | DbxException e) {
            Logging.warn("error uploading files to Dropbox.");
            e.printStackTrace();
        }
    }

}
