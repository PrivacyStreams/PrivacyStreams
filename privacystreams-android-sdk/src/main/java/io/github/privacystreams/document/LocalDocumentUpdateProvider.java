package io.github.privacystreams.document;

import java.io.File;

import io.github.privacystreams.utils.FileUpdateProvider;


class LocalDocumentUpdateProvider extends FileUpdateProvider {

    private static final String[] DOC_EXTENSION = {"txt","doc","hlp","wps","rtf","html","pdf"};

    @Override
    public void provide(){
        super.provide();
    }

    LocalDocumentUpdateProvider(){
        super(DOC_EXTENSION);
    }

    @Override
    public void onFileCreate(String path){
        File file = new File(path);
        output(new Document(file));
    }
}
