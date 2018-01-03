package io.github.privacystreams.image;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A background service for taking pictures.
 */
public class PSCameraBgService extends Service {

    private static final String TAG = "PSCameraBgService";

    private WindowManager mWindowManager;
    private HiddenCameraPreview mPreview;
    private Camera mCamera;
    private Timer mTimer;

    private static final String CAMERA_ID = "cameraId";
    private static final int CAMERA_DELAY = 1000;

    private static Callback mCallback;

    public abstract static class Callback {
        abstract void onImageTaken(byte[] imageBytes);
        abstract void onFail(boolean isFatal, String errorMessage);
    }

    public static void takePhoto(Context ctx, int cameraId, Callback callback) {
        if (mCallback != null) {
            callback.onFail(true, "camera service is busy.");
            return;
        }
        mCallback = callback;

        Intent intent = new Intent(ctx, PSCameraBgService.class);
        intent.putExtra(CAMERA_ID, cameraId);
        ctx.startService(intent);
    }

    public static void stopTakingPhoto(Context ctx, Callback callback) {
        if (mCallback == callback) mCallback = null;
        Intent intent = new Intent(ctx, PSCameraBgService.class);
        ctx.stopService(intent);
    }

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
//            Log.d(TAG, "picture taken " + data.length);
            if (mCallback != null) {
                mCallback.onImageTaken(data);
            } else {
                stopSelf();
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int cameraId = intent.getIntExtra(CAMERA_ID, 0);

        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mCamera = getCameraInstance(cameraId);
        mTimer = new Timer();

        if (mWindowManager != null && mCamera != null) {
            mPreview = new HiddenCameraPreview(this, mCamera);

            mPreview.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(1, 1,
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY :
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);
            mWindowManager.addView(mPreview, params);

            TimerTask takePhotoTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        mCamera.takePicture(null, null, mPictureCallback);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        mCallback.onFail(false, e.getMessage());
                    }
                }
            };

            mTimer.schedule(takePhotoTask, CAMERA_DELAY);
        } else {
            if (mCallback != null) {
                mCallback.onFail(true, "unable to open camera.");
            }
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    public void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
        if (mWindowManager != null) {
            mWindowManager.removeView(mPreview);
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(int id){
        Camera c = null;
        try {
            c = Camera.open(id); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.e(TAG, e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }
}
