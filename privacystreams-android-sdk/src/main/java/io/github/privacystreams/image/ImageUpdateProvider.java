package io.github.privacystreams.image;

import android.os.Environment;
import android.os.FileObserver;

import java.io.File;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Logging;
import io.github.privacystreams.utils.FileUpdateProvider;


class ImageUpdateProvider extends FileUpdateProvider {

    @Override
    public void provide(){
        super.provide();
    }

    public ImageUpdateProvider(){
        super("image");
    }

    @Override
    public void onFileCreate(String path) {
        Logging.error("a new image created");
        Long dateAdded = System.currentTimeMillis();
        ImageData imageData = ImageData.newLocalImage(new File(path));
        Image image = new Image(dateAdded, imageData);
        image.setFieldValue(Image.IMAGE_PATH,path);
        output(image);
    }
}
