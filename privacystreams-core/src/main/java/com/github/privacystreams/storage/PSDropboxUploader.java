package com.github.privacystreams.storage;

import android.Manifest;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.DropboxUtils;

/**
 * Upload an item to Dropbox
 */

final class PSDropboxUploader<Tin> extends PSFileWriter<Tin> {

    PSDropboxUploader(Function<Tin, String> filePathGenerator, boolean append) {
        super(filePathGenerator, false, append);
        this.addRequiredPermissions(Manifest.permission.INTERNET);
    }

    @Override
    public void applyInBackground(UQI uqi, Tin input) {
        super.applyInBackground(uqi, input);
        DropboxUtils.addToWaitingList(uqi, this.validFilePath);
        DropboxUtils.syncFiles(uqi, this.append);
    }
}
