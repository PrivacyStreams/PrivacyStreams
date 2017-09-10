package io.github.privacystreams.document;

import android.os.Environment;

import java.io.File;

import io.github.privacystreams.core.UQI;
import io.github.privacystreams.image.Image;
import io.github.privacystreams.image.ImageData;
import io.github.privacystreams.utils.FileUpdateProvider;
import io.github.privacystreams.utils.Logging;

/**
 * Created by Weiheng Lian on 2017/9/10.
 */

public class LocalDocumentUpdateProvider extends FileUpdateProvider {

    @Override
    public void provide(){
        super.provide();
    }

    public LocalDocumentUpdateProvider(){
        super("doc");
    }

    @Override
    public void onFileCreate(String path){
        Logging.error("a new doc created");
        File file = new File(path);
        output(new Document(file));
    }
}
