package com.github.privacystreams.document;


import com.google.api.services.drive.Drive;

interface GoogleDriveResultListener {
    void onSuccess(Drive drive);
    void onFail();
}
