package com.github.privacystreams.storage;

import android.Manifest;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.utils.Assertions;
import com.github.privacystreams.core.utils.Logging;
import com.github.privacystreams.core.utils.time.TimeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yuanchun on 27/12/2016.
 * Write the item to a file
 */

final class ExternalStorageWriter extends ItemFunction<Void> {

    private final String dirPath;
    private final String fileTag;

    ExternalStorageWriter(String dirPath, String fileTag) {
        this.dirPath = Assertions.notNull("dirPath", dirPath);
        this.fileTag = Assertions.notNull("fileTag", fileTag);
        this.addParameters(dirPath, fileTag);
        this.addRequiredPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public Void apply(UQI uqi, Item input) {
        String newFileName = fileTag + TimeUtils.now() + ".json";
        File newFile = new File(dirPath, newFileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            fileOutputStream.write(uqi.getGson().toJson(input).getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            Logging.warn("error writing item to file.");
            e.printStackTrace();
        }
        return null;
    }
}
