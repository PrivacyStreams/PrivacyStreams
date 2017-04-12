package com.github.privacystreams.image;

/**
 * A listener for camera activity.
 */

interface CameraResultListener {
    void onSuccess();
    void onFail();
    String getFilePath();
}
