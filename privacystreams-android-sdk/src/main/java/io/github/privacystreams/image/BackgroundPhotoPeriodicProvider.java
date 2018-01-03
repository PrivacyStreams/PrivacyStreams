package io.github.privacystreams.image;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;

import java.util.Timer;
import java.util.TimerTask;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.utils.Assertions;
import io.github.privacystreams.utils.Logging;
import io.github.privacystreams.utils.PermissionUtils;
import io.github.privacystreams.utils.TimeUtils;

/**
 * Periodically take photos in the background.
 */

class BackgroundPhotoPeriodicProvider extends PStreamProvider {
    private static final String TAG = "BackgroundPhotoPeriodicProvider";

    private final int cameraId;
    private final int interval;

    BackgroundPhotoPeriodicProvider(int cameraId, int interval) {
        this.cameraId = cameraId;
        this.interval = interval;
        this.addParameters(cameraId);
        this.addRequiredPermissions(Manifest.permission.CAMERA);
//        this.addRequiredPermissions(Manifest.permission.SYSTEM_ALERT_WINDOW);
        this.addRequiredPermissions(PermissionUtils.OVERLAY);
    }

    private transient boolean enabled = true;

    @Override
    protected void provide() {
        this.start();
    }

    private void start() {
        Context ctx = this.getUQI().getContext();
        PSCameraBgService.takePhoto(ctx, cameraId, mCameraCallback);
    }

    private void stop() {
        Context ctx = this.getUQI().getContext();
        PSCameraBgService.stopTakingPhoto(ctx, mCameraCallback);
    }

    private void restart() {
        this.stop();
        if (enabled) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    start();
                }
            }, interval);
        }
    }

    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        Context ctx = uqi.getContext();
        PSCameraBgService.stopTakingPhoto(ctx, mCameraCallback);
        this.enabled = false;
    }

    private PSCameraBgService.Callback mCameraCallback = new PSCameraBgService.Callback() {
        @Override
        void onImageTaken(Bitmap bitmap) {
            ImageData imageData = ImageData.newTempImage(bitmap);
            Image imageItem = new Image(TimeUtils.now(), imageData);
            output(imageItem);

            restart();
        }

        @Override
        void onFail(boolean isFatal, String errorMessage) {
            Logging.error(TAG + ": " + (isFatal? "[FATAL] " : "") + errorMessage);
            if (isFatal) {
                stop();
                enabled = false;
                raiseException(getUQI(), PSException.FAILED(errorMessage));
            } else {
                restart();
            }
        }
    };
}
