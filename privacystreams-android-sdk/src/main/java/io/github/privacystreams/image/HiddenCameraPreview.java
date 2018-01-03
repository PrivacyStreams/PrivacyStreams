package io.github.privacystreams.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

@SuppressLint("ViewConstructor")
class HiddenCameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "HiddenCameraPreview";

    private SurfaceHolder mHolder;
    private Camera mCamera;

    HiddenCameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        //Set surface holder
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        //Do nothing
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Do nothing
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
