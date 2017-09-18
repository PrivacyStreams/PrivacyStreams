package io.github.privacystreams.image;

import java.io.File;

import io.github.privacystreams.utils.FileUpdateProvider;
import io.github.privacystreams.utils.Logging;

class ImageUpdateProvider extends FileUpdateProvider {

    private static final String[] IMG_EXTENSION = {"bmp","dib","gif","jfif","jpe","jpeg",
            "jpg","png","tif","tiff","ico"};

    @Override
    public void provide(){
        super.provide();
    }

    ImageUpdateProvider(){
        super(IMG_EXTENSION);
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
