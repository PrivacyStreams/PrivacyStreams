package com.github.privacystreams.storage;

import android.Manifest;
import android.content.Context;

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

class PSFileWriter<Tin> extends AsyncFunction<Tin, Void> {

    protected final Function<Tin, String> filePathGenerator;
    protected final boolean isPublic;
    protected final boolean append;

    private static final String APPEND_SEPARATOR = "\n\n\n";

    PSFileWriter(Function<Tin, String> filePathGenerator, boolean isPublic, boolean append) {
        this.filePathGenerator = Assertions.notNull("filePathGenerator", filePathGenerator);
        this.isPublic = isPublic;
        this.append = append;

        this.addParameters(filePathGenerator, isPublic);
        if (isPublic) {
            this.addRequiredPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    protected String validFilePath = null;

    @Override
    public void applyInBackground(UQI uqi, Tin input) {
        try {
            String filePath = this.filePathGenerator.apply(uqi, input);
            File validFile = StorageUtils.getValidFile(uqi.getContext(), filePath, this.isPublic);
            this.validFilePath = validFile.getAbsolutePath();

            FileOutputStream fileOutputStream = new FileOutputStream(validFile, append);
            if (append) {
                fileOutputStream.write(APPEND_SEPARATOR.getBytes());
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
