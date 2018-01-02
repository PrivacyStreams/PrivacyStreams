/*
 * Copyright 2017 Keval Patel.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.privacystreams.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.github.privacystreams.utils.Logging;

@SuppressLint("ViewConstructor")
class HiddenCameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "HiddenCameraPreview";

    private static final String ERROR_CAMERA_OPEN_FAILED = "failed to open camera.";

    private Callbacks mCallbacks;

    private SurfaceHolder mHolder;
    private Camera mCamera;

    private volatile boolean safeToTakePicture = false;

    HiddenCameraPreview(@NonNull Context context, Callbacks callbacks) {
        super(context);

        mCallbacks = callbacks;

        //Set surface holder
        initSurfaceView();
    }

    /**
     * Initialize the surface view holder.
     */
    private void initSurfaceView() {
        mHolder = getHolder();
        mHolder.addCallback(this);
    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        //Do nothing
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //Do nothing
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (mCamera == null) {  //Camera is not initialized yet.
            mCallbacks.onCameraError(ERROR_CAMERA_OPEN_FAILED);
            return;
        } else if (surfaceHolder.getSurface() == null) { //Surface preview is not initialized yet
            mCallbacks.onCameraError(ERROR_CAMERA_OPEN_FAILED);
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // Ignore: tried to stop a non-existent preview
        }

        // Make changes in preview size
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> pictureSizes = mCamera.getParameters().getSupportedPictureSizes();

        //Sort descending
        Collections.sort(pictureSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                return (b.height * b.width) - (a.height * a.width);
            }
        });

        //set the camera image size based on config provided
        Camera.Size cameraSize = pictureSizes.get(0);   //Highest res
        parameters.setPictureSize(cameraSize.width, cameraSize.height);

        requestLayout();

        mCamera.setParameters(parameters);

        try {
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();

            safeToTakePicture = true;
        } catch (IOException | NullPointerException e) {
            //Cannot start preview
            mCallbacks.onCameraError(ERROR_CAMERA_OPEN_FAILED);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Call stopPreview() to stop updating the preview surface.
        if (mCamera != null) mCamera.stopPreview();
    }

    /**
     * Initialize the camera and start the preview of the camera.
     *
     * @param useFrontCamera whether to use front camera.
     */
    void startCameraInternal(boolean useFrontCamera) {
        int facing = useFrontCamera ? 1:0;

        if (safeCameraOpen(facing)) {
            if (mCamera != null) {
                requestLayout();

                try {
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                    mCallbacks.onCameraError(ERROR_CAMERA_OPEN_FAILED);
                }
            }
        } else {
            mCallbacks.onCameraError(ERROR_CAMERA_OPEN_FAILED);
        }
    }

    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            stopPreviewAndFreeCamera();

            mCamera = Camera.open(id);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Logging.error(TAG + ": " + ERROR_CAMERA_OPEN_FAILED);
            e.printStackTrace();
        }

        return qOpened;
    }

    boolean isSafeToTakePictureInternal() {
        return safeToTakePicture;
    }

    void takePictureInternal() {
        safeToTakePicture = false;
        if (mCamera != null) {
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(final byte[] bytes, Camera camera) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //Convert byte array to bitmap
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            mCallbacks.onImageCapture(bitmap);

                            safeToTakePicture = true;
                            mCamera.startPreview();
                        }
                    }).start();
                }
            });
        } else {
            mCallbacks.onCameraError(ERROR_CAMERA_OPEN_FAILED);
            safeToTakePicture = true;
        }
    }

    /**
     * When this function returns, mCamera will be null.
     */
    void stopPreviewAndFreeCamera() {
        safeToTakePicture = false;
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    interface Callbacks {
        void onImageCapture(@NonNull Bitmap image);
        void onCameraError(String errorMessage);
    }
}