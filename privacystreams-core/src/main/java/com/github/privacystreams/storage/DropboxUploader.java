package com.github.privacystreams.storage;

import android.Manifest;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.DropboxUtils;

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
