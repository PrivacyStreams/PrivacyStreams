package com.github.privacystreams.storage;

import android.Manifest;
import android.content.Context;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.AsyncFunction;
import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;
import com.github.privacystreams.utils.Logging;
import com.github.privacystreams.utils.StorageUtils;
import com.github.privacystreams.utils.time.TimeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Write an Item to a file
 */

class FileWriter<Tin> extends AsyncFunction<Tin, Void> {

    private final String dirPath;
    private final String fileTag;

    FileWriter(String dirPath, String fileTag) {
        this.dirPath = dirPath;
        this.fileTag = Assertions.notNull("fileTag", fileTag);
        this.addParameters(dirPath, fileTag);
        if (dirPath != null) {
            this.addRequiredPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    protected transient String newFileName;
    @Override
    public void applyInBackground(UQI uqi, Tin input) {
        this.newFileName = fileTag + "_" + TimeUtils.getTimeTag() + ".json";
        try {
            FileOutputStream fileOutputStream;
            if (this.dirPath == null) {
                fileOutputStream = uqi.getContext().openFileOutput(this.newFileName, Context.MODE_PRIVATE);
            }
            else {
                File newFileDir = StorageUtils.getPublicDir(this.dirPath);
                File newFile = new File(newFileDir, this.newFileName);
                fileOutputStream = new FileOutputStream(newFile);
            }
            fileOutputStream.write(uqi.getGson().toJson(input).getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            Logging.warn("error writing data to file.");
            e.printStackTrace();
        }
    }

    @Override
    protected Void init(UQI uqi, Tin input) {
        return null;
    }

}
