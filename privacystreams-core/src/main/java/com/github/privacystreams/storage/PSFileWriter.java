package com.github.privacystreams.storage;

import android.Manifest;

import com.github.privacystreams.core.AsyncFunction;
import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;
import com.github.privacystreams.utils.StorageUtils;

import java.io.File;

/**
 * Write an Item to a file
 */

class PSFileWriter<Tin> extends AsyncFunction<Tin, Void> {

    protected final Function<Tin, String> filePathGenerator;
    protected final boolean isPublic;
    protected final boolean append;

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
        String filePath = this.filePathGenerator.apply(uqi, input);
        File validFile = StorageUtils.getValidFile(uqi.getContext(), filePath, this.isPublic);
        this.validFilePath = validFile.getAbsolutePath();
        StorageUtils.writeToFile(input.toString(), validFile, append);
    }

    @Override
    protected Void init(UQI uqi, Tin input) {
        return null;
    }

}
