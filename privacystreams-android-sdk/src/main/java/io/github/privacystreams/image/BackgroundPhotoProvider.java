package io.github.privacystreams.image;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.audio.Audio;
import io.github.privacystreams.audio.AudioData;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.utils.Globals;
import io.github.privacystreams.utils.Logging;
import io.github.privacystreams.utils.PermissionUtils;
import io.github.privacystreams.utils.StorageUtils;
import io.github.privacystreams.utils.TimeUtils;

/**
 * Take a photo in the background.
 */

class BackgroundPhotoProvider extends PStreamProvider implements HiddenCameraPreview.Callbacks {
    private final boolean useFrontCamera;

    BackgroundPhotoProvider(boolean useFrontCamera) {
        this.useFrontCamera = useFrontCamera;
        this.addParameters(useFrontCamera);
        this.addRequiredPermissions(Manifest.permission.CAMERA);
//        this.addRequiredPermissions(Manifest.permission.SYSTEM_ALERT_WINDOW);
        this.addRequiredPermissions(PermissionUtils.OVERLAY);
    }

    @Override
    protected void provide() {
        //create fake camera view
        Context ctx = this.getUQI().getContext();

        if (useFrontCamera && !this.isFrontCameraAvailable(ctx)) {
            this.onCameraError("front camera is not available.");
            return;
        }

        WindowManager mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);

        if (mWindowManager != null) {
            Looper.prepare();

            HiddenCameraPreview cameraPreview = new HiddenCameraPreview(ctx, this);
            cameraPreview.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(1, 1,
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY :
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);
            mWindowManager.addView(cameraPreview, params);
            cameraPreview.startCameraInternal(useFrontCamera);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (cameraPreview.isSafeToTakePictureInternal()) {
                cameraPreview.takePictureInternal();
            } else {
                this.onCameraError("camera is busy right now.");
            }

            mWindowManager.removeView(cameraPreview);
            cameraPreview.stopPreviewAndFreeCamera();
        } else {
            this.onCameraError("mWindowManager is null.");
        }
    }

    private boolean isFrontCameraAvailable(@NonNull Context context) {
        int numCameras = Camera.getNumberOfCameras();
        return numCameras > 0 && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }

    @Override
    public void onImageCapture(@NonNull Bitmap image) {
        ImageData imageData = ImageData.newTempImage(image);
        Image imageItem = new Image(TimeUtils.now(), imageData);
        this.output(imageItem);
        this.finish();
    }

    @Override
    public void onCameraError(String errorMessage) {
        Logging.error(errorMessage);
        this.raiseException(this.getUQI(), PSException.INTERRUPTED(errorMessage));
    }
}
