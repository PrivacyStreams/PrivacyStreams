package io.github.privacystreams.utils;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.UQI;

public class FileUpdateProvider extends PStreamProvider
{
    private final String[] IMG_EXTENSION = {"bmp","dib","gif","jfif","jpe","jpeg",
            "jpg","png","tif","tiff","ico"};

    private final String[] DOC_EXTENSION = {"txt","doc","hlp","wps","rtf","html","pdf"};

    private String type;

    private RecursiveFileObserver mFileObserver;

    @Override
    public void provide(){
        setupFileObserver();
    }

    public FileUpdateProvider(String type){
        this.type = type;
    }

    public void onFileCreate(String path){

    }

    private void judgeType(String path){
        int dot = path.lastIndexOf(".");
        String suffix = path.substring(dot+1);
        switch(type){
            case "image":
                for(String extension : IMG_EXTENSION){
                    if(extension.equals(suffix)){
                        onFileCreate(path);
                        break;
                    }
                }
                break;
            case "doc":
                for(String extension : DOC_EXTENSION){
                    if(extension.equals(suffix)){
                        onFileCreate(path);
                        break;
                    }
                }
                break;
            default:
        }
    }

    private void setupFileObserver(){
        mFileObserver = new RecursiveFileObserver(){
            @Override
            public void onFileCreate(String path) {
                judgeType(path);
            }
        };
        mFileObserver.startWatching();
    }

    @Override
    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        if(mFileObserver!= null)
            mFileObserver.stopWatching();
    }
}

