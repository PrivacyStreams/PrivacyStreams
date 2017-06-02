package com.github.privacystreams.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.location.LatLon;
import com.github.privacystreams.utils.ImageUtils;
import com.github.privacystreams.utils.Logging;
import com.github.privacystreams.utils.StorageUtils;
import com.github.privacystreams.utils.TimeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

/**
 * An abstraction of image data.
 */

public class ImageData {
    private static final String LOG_TAG = "ImageData: ";

    private final int type;

    private static final int TYPE_TEMP_FILE = 0;
    private static final int TYPE_TEMP_BITMAP = 1;
    private static final int TYPE_LOCAL_FILE = 2;
    private static final int TYPE_REMOTE_FILE = 3;

    private transient File imageFile;
    private transient Bitmap bitmap;

    private transient ExifInterface exifInterface;
    private transient LatLon latLon;
    private transient String filePath;
    private transient ImageData blurredImageData;

    private ImageData(int type) {
        this.type = type;
    }

    static ImageData newTempImage(File tempImageFile) {
        ImageData imageData = new ImageData(TYPE_TEMP_FILE);
        imageData.imageFile = tempImageFile;
        return imageData;
    }

    static ImageData newTempImage(Bitmap tempBitmap) {
        ImageData imageData = new ImageData(TYPE_TEMP_BITMAP);
        imageData.bitmap = tempBitmap;
        return imageData;
    }

    static ImageData newLocalImage(File localImageFile) {
        ImageData imageData = new ImageData(TYPE_LOCAL_FILE);
        imageData.imageFile = localImageFile;
        return imageData;
    }

    String getFilepath(UQI uqi) {
        if (this.filePath != null) return this.filePath;

        if (this.imageFile != null) {
            this.filePath = this.imageFile.getAbsolutePath();
        }
        else if (this.bitmap != null) {
            String imagePath = "temp/image_" + TimeUtils.getTimeTag() + ".jpg";
            this.imageFile = StorageUtils.getValidFile(uqi.getContext(), imagePath, false);
            try {
                FileOutputStream out = new FileOutputStream(this.imageFile);
                this.bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.filePath = this.imageFile.getAbsolutePath();
        }
        else {
            Logging.warn(LOG_TAG + "Both file path and bitmap don't exist.");
        }

        return this.filePath;
    }

    ExifInterface getExif(UQI uqi) {
        if (this.exifInterface != null)
            return this.exifInterface;

        String filePath = this.getFilepath(uqi);
        if (filePath == null) return null;

        try {
            this.exifInterface = new ExifInterface(filePath);
        } catch (Exception e) {
            Logging.warn(e.getMessage());
        }
        return this.exifInterface;
    }

    LatLon getLatLon(UQI uqi) {
        if (this.latLon != null) return this.latLon;

        ExifInterface exifInterface = this.getExif(uqi);
        if (exifInterface == null) return null;
        float[] latLong = new float[2];
        boolean hasLatLong = exifInterface.getLatLong(latLong);
        if(hasLatLong) {
            this.latLon = new LatLon((double) latLong[0], (double) latLong[1]);
        }
        return this.latLon;
    }

    Bitmap getBitmap(UQI uqi) {
        if (this.bitmap != null) return this.bitmap;

        String filePath = this.getFilepath(uqi);
        if (filePath == null) return null;

        this.bitmap = BitmapFactory.decodeFile(filePath);
        return this.bitmap;
    }

    Boolean hasFace(UQI uqi) {
        // TODO implement this.
        return false;
    }

    Boolean hasCharacter(UQI uqi) {
        // TODO implement this.
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    ImageData getBlurred(UQI uqi) {
        if (this.blurredImageData != null) return this.blurredImageData;

        Bitmap bitmap = this.getBitmap(uqi);
        if (bitmap == null) return null;
        Bitmap blurredBitmap = ImageUtils.blur(uqi, bitmap);
        return ImageData.newTempImage(blurredBitmap);
    }

    public String toString() {
        return String.format(Locale.getDefault(), "<Image@%d%d>", this.type, this.hashCode());
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (this.type != TYPE_LOCAL_FILE) {
            if (this.imageFile != null && this.imageFile.exists()) {
                StorageUtils.safeDelete(this.imageFile);
            }
        }
    }
}
