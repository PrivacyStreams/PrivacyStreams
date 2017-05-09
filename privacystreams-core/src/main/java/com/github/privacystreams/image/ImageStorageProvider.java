package com.github.privacystreams.image;

import android.Manifest;
import android.database.Cursor;
import android.provider.MediaStore;

import com.github.privacystreams.core.providers.MStreamProvider;

import java.io.File;

/**
 * Provide a stream of images stored in local sd card.
 */

class ImageStorageProvider extends MStreamProvider {

    ImageStorageProvider(){
        this.addRequiredPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    protected void provide() {
        this.getImageInfo();
        this.finish();

    }

    private void getImageInfo(){

        Cursor c = this.getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATA
                },
                null, null, null
        );

        if (c!=null && c.moveToFirst()) {
            do {
                // Get the field values
                int bucketId = c.getInt(0);
                String bucketName = c.getString(1);
                int imageId = c.getInt(2);
                Long dateAdded = c.getLong(3);
                long size = c.getLong(4);
                String imageName = c.getString(5);
                String filePath = c.getString(6);
                ImageData imageData = ImageData.newLocalImage(new File(filePath));
                Image image = new Image(dateAdded, imageData);
                image.setFieldValue(Image.BUCKET_ID, bucketId);
                image.setFieldValue(Image.BUCKET_NAME, bucketName);
                image.setFieldValue(Image.IMAGE_ID, imageId);
                image.setFieldValue(Image.IMAGE_NAME, imageName);
                image.setFieldValue(Image.IMAGE_PATH, filePath);
                this.output(image);
            } while (c.moveToNext());

            c.close();
        }
    }

}

