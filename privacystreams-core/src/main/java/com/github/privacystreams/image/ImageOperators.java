package com.github.privacystreams.image;

import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.location.LatLon;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access image-related operators
 */
@PSOperatorWrapper
public class ImageOperators {

    /**
     * Retrieve the EXIF of the image specified by an ImageData field.
     * The EXIF information is an instance of Android `ExifInterface` class.
     *
     * @param imageDataField the name of ImageData field
     * @return the function
     */
    public static Function<Item, ExifInterface> getExif(String imageDataField) {
        return new ImageExifRetriever(imageDataField);
    }

    /**
     * Retrieve the location information of the image specified by an ImageData field.
     * The location information is an instance of `LatLon` class.
     *
     * @param imageDataField the name of ImageData field
     * @return the function
     */
    public static Function<Item, LatLon> getLatLon(String imageDataField) {
        return new ImageLatLonRetriever(imageDataField);
    }

    /**
     * Get the file path of the image specified by an ImageData field.
     * The path might point to a temporary image file if it is not from storage.
     * To permanently save the file, you need to copy the file to another file path.
     *
     * @param imageDataField the name of ImageData field
     * @return the function
     */
    public static Function<Item, String> getFilepath(String imageDataField) {
        return new ImageFilepathGetter(imageDataField);
    }

    /**
     * Get the Bitmap of the image specified by an ImageData field.
     *
     * @param imageDataField the name of ImageData field
     * @return the function
     */
    public static Function<Item, Bitmap> getBitmap(String imageDataField) {
        return new ImageBitmapGetter(imageDataField);
    }

    /**
     * Blur the image specified by an ImageData field,
     * and return the blurred ImageData instance.
     *
     * @param imageDataField the name of ImageData field
     * @return the function
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Function<Item, ImageData> blur(String imageDataField) {
        return new ImageBlurFunction(imageDataField);
    }

    /**
     * Detect faces in an image.
     * This operator outputs true if there is at least one face in the image.
     *
     * @param imageDataField the name of ImageData field
     * @return the function
     */
    public static Function<Item, Boolean> hasFace(String imageDataField) {
        return new ImageFaceDetector(imageDataField);
    }

    /**
     * Detect characters in an image.
     * This operator outputs true if there is at least one character in the image.
     *
     * @param imageDataField the name of ImageData field
     * @return the function
     */
    public static Function<Item, Boolean> hasCharacter(String imageDataField) {
        return new ImageCharacterDetector(imageDataField);
    }
}
