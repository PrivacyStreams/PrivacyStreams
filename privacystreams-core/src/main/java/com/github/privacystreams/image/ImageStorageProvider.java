package com.github.privacystreams.image;

import android.Manifest;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.Logging;

import java.io.IOException;


/**
 * Created by fanglinchen on 2/2/17.
 */

public class ImageStorageProvider extends MultiItemStreamProvider {

    ImageStorageProvider(){
        this.addRequiredPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    protected void provide() {
        this.getImageInfo(output);

    }

    private void getImageInfo(MultiItemStream output){
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
                    exifLatitude = Double.valueOf(exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
                    exifLongitude = Double.valueOf(exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
                }
                catch (IOException exception){
                    Logging.debug(exception.toString());
                }

            } while (cur.moveToNext());
            Image image = new Image(date,
                    Uri.parse(dataUri),exifLatitude, exifLongitude);
            this.output(image);
            cur.close();
        }

        if (!output.isClosed()) this.output(Item.EOS);
    }

}

