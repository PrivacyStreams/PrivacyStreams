package com.github.privacystreams.storage;

import android.Manifest;
import android.content.Context;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;
import com.github.privacystreams.utils.Consts;
import com.github.privacystreams.utils.DropboxUtils;
import com.github.privacystreams.utils.Logging;
import com.github.privacystreams.utils.time.TimeUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Upload an item to Dropbox
 */

final class DropboxUploader<Tin> extends FileWriter<Tin> {

    DropboxUploader(String fileTag) {
        super(null, fileTag);
        this.addRequiredPermissions(Manifest.permission.INTERNET);
    }

    @Override
    public void applyInBackground(UQI uqi, Tin input) {
        super.applyInBackground(uqi, input);
        DropboxUtils.addToWaitingList(uqi, this.newFileName);
        DropboxUtils.syncFiles(uqi, false);
    }
}
