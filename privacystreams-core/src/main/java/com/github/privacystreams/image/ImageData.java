package com.github.privacystreams.image;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * An abstraction of audio instance.
 */

public class ImageData {
    private int type;
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_LOCAL_FILE = 1;
    private static final int TYPE_REMOTE_FILE = 1;

    private File tempImageFile;

    public String toString() {
        String fileName = this.tempImageFile.getName();
        String imageTag = fileName.substring(0, fileName.lastIndexOf('.'));

        return String.format(Locale.getDefault(), "<Image@%s>", imageTag);
    }
}
