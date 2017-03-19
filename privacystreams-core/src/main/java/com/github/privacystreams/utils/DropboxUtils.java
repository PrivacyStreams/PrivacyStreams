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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A helper class to access Dropbox-related utility functions.
 */

public class DropboxUtils {

    private static final String DROPBOX_FILE_PREFIX = "dropbox";
    private static final String WORD_SEPARATOR = "__";

    private static final String DROPBOX_WAITING_LIST = "dropbox_waiting_list";

    private static long lastSyncTimestamp = 0;

    public static void addToWaitingList(UQI uqi, String fileName) {
        SharedPreferences pref = uqi.getContext().getSharedPreferences(Consts.LIB_TAG, Context.MODE_APPEND);
        Set<String> waitingList = pref.getStringSet(DROPBOX_WAITING_LIST, new HashSet<String>());
        waitingList.add(fileName);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(DROPBOX_WAITING_LIST, waitingList);
        editor.apply();
        Logging.debug("Added file to Dropbox waiting list: " + fileName);
    }

    public static synchronized void syncFiles(UQI uqi) {
        long currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp - lastSyncTimestamp < GlobalConfig.DropboxConfig.leastSyncInterval)
            return;

        try {
            SharedPreferences pref = uqi.getContext().getSharedPreferences(Consts.LIB_TAG, Context.MODE_APPEND);
            Set<String> filesToUpload = pref.getStringSet(DROPBOX_WAITING_LIST, new HashSet<String>());

            Logging.debug("Trying to upload files to Dropbox: " + filesToUpload);

            if (filesToUpload.isEmpty()) return;
            if (GlobalConfig.DropboxConfig.onlyOverWifi && !ConnectionUtils.isWifiConnected(uqi)) return;

            // Create Dropbox client
            DbxRequestConfig config = new DbxRequestConfig(Consts.LIB_TAG);
            DbxClientV2 client = new DbxClientV2(config, GlobalConfig.DropboxConfig.accessToken);

            Auth.getOAuth2Token();

            for (String fileToUpload : filesToUpload) {
                FileInputStream inputStream = uqi.getContext().openFileInput(fileToUpload);
                client.files().uploadBuilder("/" + uqi.getUUID() + "/" + fileToUpload)
                        .withMode(WriteMode.ADD)
                        .uploadAndFinish(inputStream);
                inputStream.close();
                uqi.getContext().deleteFile(fileToUpload);
            }

            lastSyncTimestamp = System.currentTimeMillis();

            Logging.debug("Upload finished.");

        } catch (IOException | DbxException e) {
            Logging.warn("error uploading files to Dropbox.");
            e.printStackTrace();
        }
    }

}
