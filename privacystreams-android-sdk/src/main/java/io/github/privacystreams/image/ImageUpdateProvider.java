package io.github.privacystreams.image;

import android.os.Environment;
import android.os.FileObserver;

import java.io.File;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Logging;


class ImageUpdateProvider extends PStreamProvider {

    private final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

    private final String[] IMG_EXTENSION = {"bmp","dib","gif","jfif","jpe","jpeg",
            "jpg","png","tif","tiff","ico"};

    private RecursiveFileObserver mFileObserver;

    @Override
    public void provide(){
        setupFileObserver();
    }

    private void setupFileObserver(){
        Logging.error("path is: "+ROOT);
        mFileObserver = new RecursiveFileObserver(ROOT,FileObserver.CREATE) {
            @Override
            public void onFileCreate(String path) {
                Logging.error("create path is: "+path);
                boolean isImage = false;
                int dot = path.lastIndexOf(".");
                String suffix = path.substring(dot+1);
                for(String extension : IMG_EXTENSION){
                    if(suffix.equals(extension)){
                        isImage = true;
                        break;
                    }
                }
                if(isImage){
                    Logging.error("a new image stored");
                    Long dateAdded = System.currentTimeMillis();
                    ImageData imageData = ImageData.newLocalImage(new File(ROOT+"/"+path));
                    Image image = new Image(dateAdded, imageData);
                    image.setFieldValue(Image.IMAGE_PATH,ROOT+"/"+path);
                    output(image);
                }
                else{
                    Logging.error("the new file is not an image");
                }
            }
        };
        mFileObserver.startWatching();
    }

    @Override
    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        stopWatching();
    }

    private void stopWatching(){
        if(mFileObserver!=null){
            mFileObserver.stopWatching();
        }
    }

}
