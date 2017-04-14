package com.github.privacystreams.image;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.github.privacystreams.utils.Logging;
import com.github.privacystreams.utils.PSFileProvider;

import java.io.File;

/**
 * This Activity is to take a picture using camera.
 */
public class PSCameraActivity extends Activity {

    private static final String TAG = "PSCameraActivity - ";

    private static final int REQUEST_CODE = 1;

    private static CameraResultListener cameraResultListener;

    static void setListener(CameraResultListener cameraResultListener) {
        PSCameraActivity.cameraResultListener = cameraResultListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (cameraResultListener != null && cameraResultListener.getFilePath() != null) {
            File tempImageFile = new File(cameraResultListener.getFilePath());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = PSFileProvider.getUriForFile(this, PSFileProvider.getProviderName(this), tempImageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            this.startActivityForResult(intent, REQUEST_CODE);
        } else {
            Intent result = new Intent();
            setResult(Activity.RESULT_CANCELED, result);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
//                Logging.debug(TAG + "OK.");
                cameraResultListener.onSuccess();
            }
            else {
                Logging.warn(TAG + "failed with result code: " + resultCode);
                cameraResultListener.onFail();
            }
        }
        else {
            Logging.warn(TAG + "unknown request code: " + requestCode);
        }
        PSCameraActivity.cameraResultListener = null;
        finish();
    }
}
