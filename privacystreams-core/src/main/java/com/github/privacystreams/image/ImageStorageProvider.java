package com.github.privacystreams.image;

import android.Manifest;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.Logging;

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

        Cursor cur = this.getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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

        if (cur!=null && cur.moveToFirst()) {
            ExifInterface exifInterface;

            String date;
            String dataUri;
            double exifLatitude =-1;
            double exifLongitude =-1;

            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);

            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);

            do {
                // Get the field values
                date = cur.getString(dateColumn);
                dataUri = cur.getString(dataColumn);
                try{
                    exifInterface = new ExifInterface(dataUri);
                    float[] latLong = new float[2];
                    boolean hasLatLong = exifInterface.getLatLong(latLong);
                    if(hasLatLong){
                        exifLatitude = (double) latLong[0];
                        exifLongitude = (double) latLong[1];
                    }
                }
                catch (IOException | NullPointerException exception){
                    Logging.debug(exception.toString());
                }
                Image image = new Image(date,
                        Uri.parse(dataUri),exifLatitude, exifLongitude);
                this.output(image);
            } while (cur.moveToNext());

            cur.close();
        }
    }

}

