package io.github.privacystreams.image;

import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.FileObserver;

import java.io.File;
import java.util.List;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.Logging;

/**
 * Created by Weiheng Lian on 2017/9/8.
 */

public class ImageUpdateProvider extends PStreamProvider {

    private final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    private final String[] IS_IMAGE = {"bmp","dib","gif","jfif","jpe","jpeg","jpg","png","tif","tiff","ico"};
    private RecursiveFileObserver mFileObserver;
    @Override
    public void provide(){
        Logging.error("start");
        setupFileObserver();
    }

    public void setupFileObserver(){
        Logging.error("path is: "+ROOT);
        mFileObserver = new RecursiveFileObserver(ROOT,FileObserver.CREATE) {
            @Override
            public void onFileCreate(String path) {
                Logging.error("create path is: "+path);
                boolean isImage = false;
                int dot = path.lastIndexOf(".");
                String suffix = path.substring(dot+1);
                for(int i=0;i<IS_IMAGE.length;++i){
                    if(suffix.equals(IS_IMAGE[i])){
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

    public void stopWatching(){
        if(mFileObserver!=null){
            mFileObserver.stopWatching();
        }
    }

}
