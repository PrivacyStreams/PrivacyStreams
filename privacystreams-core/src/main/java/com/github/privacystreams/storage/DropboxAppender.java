package com.github.privacystreams.storage;

import android.Manifest;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.DropboxUtils;

/**
 * Upload an item to Dropbox
 */

final class DropboxAppender<Tin> extends FileAppender<Tin> {

    DropboxAppender(String fileName) {
        super(null, fileName);
        this.addRequiredPermissions(Manifest.permission.INTERNET);
    }

    @Override
    public void applyInBackground(UQI uqi, Tin input) {
        super.applyInBackground(uqi, input);
        DropboxUtils.addToWaitingList(uqi, this.fileName);
        DropboxUtils.syncFiles(uqi);
    }
}
