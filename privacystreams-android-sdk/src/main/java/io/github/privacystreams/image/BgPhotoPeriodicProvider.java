package io.github.privacystreams.image;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;

import java.util.Timer;
import java.util.TimerTask;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.utils.AlarmScheduler;
import io.github.privacystreams.utils.Globals;
import io.github.privacystreams.utils.Logging;
import io.github.privacystreams.utils.PermissionUtils;
import io.github.privacystreams.utils.TimeUtils;

/**
 * Periodically take photos in the background.
 */

class BgPhotoPeriodicProvider extends PStreamProvider {
    private static final String TAG = "BgPhotoPeriodicProvider";

    private final int cameraId;
    private final long interval;

    BgPhotoPeriodicProvider(int cameraId, long interval) {
        this.cameraId = cameraId;
        this.interval = interval;
        this.addParameters(cameraId);
        this.addRequiredPermissions(Manifest.permission.CAMERA);
//        this.addRequiredPermissions(Manifest.permission.SYSTEM_ALERT_WINDOW);
        this.addRequiredPermissions(PermissionUtils.OVERLAY);
    }

    private transient boolean enabled = true;

    private transient AlarmScheduler alarmScheduler;

    @Override
    protected void provide() {
        final Context ctx = getContext().getApplicationContext();

        if (Globals.ImageConfig.bgUseAlarmScheduler) {
            alarmScheduler = new AlarmScheduler(ctx, this.getClass().getName()) {
                @Override
                protected void run() {
                    start(ctx);
                }
            };
        }
        this.start(ctx);
    }

    private void start(Context ctx) {
        PSCameraBgService.takePhoto(ctx, cameraId, mCameraCallback);
    }

    private void stop(Context ctx) {
        PSCameraBgService.stopTakingPhoto(ctx, mCameraCallback);
    }

    private void restart(final Context ctx) {
        this.stop(ctx);
        if (enabled) {
            if (Globals.ImageConfig.bgUseAlarmScheduler) {
                alarmScheduler.schedule(interval);
            } else {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        start(ctx);
                    }
                }, interval);
            }
        }
    }

    private void destroy(Context ctx) {
        this.stop(ctx);
        this.enabled = false;
        if (Globals.ImageConfig.bgUseAlarmScheduler) {
            try {
                alarmScheduler.destroy();
            } catch (Exception e) {
                Logging.error(e.getMessage());
            }
        }
    }

    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        Context ctx = uqi.getContext();
        this.destroy(ctx);
    }

    private PSCameraBgService.Callback mCameraCallback = new PSCameraBgService.Callback() {
        @Override
        void onImageTaken(byte[] imageBytes) {
            ImageData imageData = ImageData.newTempImage(imageBytes);
            Image imageItem = new Image(TimeUtils.now(), imageData);
            output(imageItem);

            restart(getContext());
        }

        @Override
        void onFail(boolean isFatal, String errorMessage) {
            Logging.error(TAG + ": " + (isFatal? "[FATAL] " : "") + errorMessage);
            if (isFatal) {
                destroy(getContext());
                raiseException(getUQI(), PSException.FAILED(errorMessage));
            } else {
                restart(getContext());
            }
        }
    };

}
