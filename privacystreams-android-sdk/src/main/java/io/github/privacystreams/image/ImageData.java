package io.github.privacystreams.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.FaceDetector;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.SparseArray;

import io.github.privacystreams.core.UQI;
import io.github.privacystreams.location.LatLon;
import io.github.privacystreams.utils.ImageUtils;
import io.github.privacystreams.utils.Logging;
import io.github.privacystreams.utils.StorageUtils;
import io.github.privacystreams.utils.TimeUtils;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
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
    private static final int TYPE_TEMP_BYTES = 4;

    private transient File imageFile;
    private transient Bitmap bitmap;
    private transient byte[] bytes;

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

    static ImageData newTempImage(byte[] bytes) {
        ImageData imageData = new ImageData(TYPE_TEMP_BYTES);
        imageData.bytes = bytes;
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
        else if (this.bytes != null) {
            String imagePath = "temp/image_" + TimeUtils.getTimeTag() + ".jpg";
            this.imageFile = StorageUtils.getValidFile(uqi.getContext(), imagePath, false);
            try {
                FileOutputStream out = new FileOutputStream(this.imageFile);
                out.write(this.bytes);
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

    private transient Bitmap bitmap565;
    Bitmap getBitmapRGB565(UQI uqi) {
        if (this.bitmap565 != null) return this.bitmap565;

        String filePath = this.getFilepath(uqi);
        if (filePath == null) return null;
        BitmapFactory.Options bitmapFatoryOptions = new BitmapFactory.Options();
        bitmapFatoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        this.bitmap565 = BitmapFactory.decodeFile(filePath, bitmapFatoryOptions);
        return this.bitmap565;
    }

    List<FaceDetector.Face> getFaces(UQI uqi) {
        int max = 10;
        List<FaceDetector.Face> faces = new ArrayList<>();
        Bitmap bitmap = this.getBitmapRGB565(uqi);
        if (bitmap == null) return faces;
        FaceDetector detector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), max);
        FaceDetector.Face[] facesArray = new FaceDetector.Face[max];
        int count = detector.findFaces(bitmap, facesArray);
        for (int i = 0; i < count; i++) {
            FaceDetector.Face face = facesArray[i];
            if (face != null && face.confidence() > 0.3)
                faces.add(face);
        }
        return faces;
    }

    List<TextBlock> detectTextBlocks(UQI uqi) {
        List<TextBlock> result = new ArrayList<>();
        Bitmap bitmap = this.getBitmap(uqi);
        if (bitmap == null) return result;
        TextRecognizer textRecognizer = new TextRecognizer.Builder(uqi.getContext()).build();
        if (!textRecognizer.isOperational()) {
            Logging.warn("TextRecognizer is not operational");
            textRecognizer.release();
            return result;
        }
        Frame imageFrame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
            result.add(textBlock);
        }
        textRecognizer.release();
        return result;
    }

    Integer countFaces(UQI uqi) {
        return this.getFaces(uqi).size();
    }

    Boolean hasFace(UQI uqi) {
        return this.countFaces(uqi) > 0;
    }

    String detectText(UQI uqi) {
        StringBuilder text = new StringBuilder();
        for (TextBlock textBlock : this.detectTextBlocks(uqi)) {
            text.append(textBlock.getValue()).append("\n");
        }
        return text.toString();
    }

    Boolean hasCharacter(UQI uqi) {
        return !this.detectTextBlocks(uqi).isEmpty();
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
