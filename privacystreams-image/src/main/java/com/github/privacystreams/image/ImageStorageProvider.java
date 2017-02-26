package com.github.privacystreams.image;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.core.utils.Logging;


/**
 * Created by fanglinchen on 2/2/17.
 */

public class ImageStorageProvider extends MultiItemStreamProvider {

    ImageStorageProvider(){
        this.addRequiredPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    protected void provide(MultiItemStream output) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            this.getImageInfo(output);
        else
            Logging.warn("Need storage reading permission");
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
            long size;
            String date;
            String name;
            String dataUri;

            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);

            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);

            do {
                // Get the field values
                date = cur.getString(dateColumn);
                dataUri = cur.getString(dataColumn);

            } while (cur.moveToNext());
            Image image = new Image(date, Uri.parse(dataUri));
            output.write(image);
            cur.close();
        }

        if (!output.isClosed()) output.write(null);
    }

}

