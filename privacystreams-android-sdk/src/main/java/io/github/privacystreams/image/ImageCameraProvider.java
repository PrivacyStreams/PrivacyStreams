package io.github.privacystreams.image;

import android.Manifest;
import android.content.Intent;

import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.StorageUtils;
import io.github.privacystreams.utils.TimeUtils;


import java.io.File;

/**
 * Provide an PStream with an Image item, which represents a photo taken from camera.
 */

class ImageCameraProvider extends PStreamProvider implements CameraResultListener {

    ImageCameraProvider() {
        this.addRequiredPermissions(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void provide() {
        this.takePhoto();

    }

    transient String imagePath;

    private void takePhoto() {
        String imagePath = "PrivacyStreams/image_" + TimeUtils.getTimeTag() + ".jpg";
        File tempImageFile = StorageUtils.getValidFile(this.getContext(), imagePath, true);
        this.imagePath = tempImageFile.getAbsolutePath();
        PSCameraActivity.setListener(this);
        Intent intent = new Intent(this.getContext(), PSCameraActivity.class);
        this.getContext().startActivity(intent);
    }

    public void onSuccess() {
        ImageData imageData = ImageData.newLocalImage(new File(this.imagePath));
        Image image = new Image(System.currentTimeMillis(), imageData);
        this.output(image);
        this.finish();
    }

    @Override
    public void onFail() {
        this.finish();
        this.raiseException(this.getUQI(), PSException.INTERRUPTED("Camera canceled."));
    }

    @Override
    public String getFilePath() {
        return imagePath;
    }


}
