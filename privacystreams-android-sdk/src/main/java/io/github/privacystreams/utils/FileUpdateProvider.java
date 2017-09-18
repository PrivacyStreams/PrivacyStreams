package io.github.privacystreams.utils;

import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.UQI;

public class FileUpdateProvider extends PStreamProvider
{
    private String[] mExtensions;

    private RecursiveFileObserver mFileObserver;

    @Override
    public void provide(){
        setupFileObserver();
    }

    public FileUpdateProvider(String[] extension){
        mExtensions = extension;
    }

    public void onFileCreate(String path){

    }

    private void judgeType(String path){
        int dot = path.lastIndexOf(".");
        String suffix = path.substring(dot+1);
        for(String extension : mExtensions) {
            if (extension.equals(suffix)) {
                onFileCreate(path);
                break;
            }
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

