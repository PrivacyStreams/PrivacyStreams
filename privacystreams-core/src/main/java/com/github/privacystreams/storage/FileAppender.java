package com.github.privacystreams.storage;

import android.Manifest;
import android.content.Context;

import com.github.privacystreams.core.AsyncFunction;
import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;
import com.github.privacystreams.utils.Logging;
import com.github.privacystreams.utils.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Write an Item to a file
 */

class FileAppender<Tin> extends AsyncFunction<Tin, Void> {

    private final String dirPath;
    protected final String fileName;

    private static final String SEPARATOR = "\n\n\n";

    FileAppender(String dirPath, String fileName) {
        this.dirPath = dirPath;
        this.fileName = Assertions.notNull("fileName", fileName);
        this.addParameters(dirPath, fileName);
        if (dirPath != null) {
            this.addRequiredPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected Void init(UQI uqi, Tin input) {
        return null;
    }

    public void applyInBackground(UQI uqi, Tin input) {
        try {
            FileOutputStream fileOutputStream;
            if (this.dirPath == null) {
                fileOutputStream = uqi.getContext().openFileOutput(this.fileName, Context.MODE_APPEND);
            }
            else {
                File newFileDir = StorageUtils.getPublicDir(this.dirPath);
                File newFile = new File(newFileDir, this.fileName);
                fileOutputStream = new FileOutputStream(newFile, true);
            }
            fileOutputStream.write(SEPARATOR.getBytes());
            fileOutputStream.write(uqi.getGson().toJson(input).getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            Logging.warn("error writing data to file.");
            e.printStackTrace();
        }
    }
}
