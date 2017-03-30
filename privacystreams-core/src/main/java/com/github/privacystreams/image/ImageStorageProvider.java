package com.github.privacystreams.image;

import android.Manifest;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.Logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
                        MediaStore.Images.Media.DATE_TAKEN,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATA
                },
                null, null, null
        );

        if (c!=null && c.moveToFirst()) {
            String dateTaken;
            String fileName;

            do {
                // Get the field values
                dateTaken = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                fileName = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                ImageData imageData = ImageData.newLocalImage(new File(fileName));
                Image image = new Image(dateTaken, imageData);
                this.output(image);
            } while (c.moveToNext());

            c.close();
        }
    }

}

