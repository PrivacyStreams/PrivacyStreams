package io.github.privacystreams.utils;

import android.os.Environment;
import android.os.FileObserver;
import android.util.ArrayMap;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import io.github.privacystreams.utils.Logging;

/**
 * This class is used to listen all the files created under the root path of sd card.
 * It takes about several seconds to initialize (The actual number depends on the phone).
 */

public class RecursiveFileObserver extends FileObserver
{
    private static Map<String, SingleFileObserver> mObservers;
    private String mPath;
    private int mMask;

    public RecursiveFileObserver()
    {
        super(Environment.getExternalStorageDirectory().getAbsolutePath(), ALL_EVENTS);
        mPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mMask = ALL_EVENTS;
    }

    @Override public void startWatching()
    {
        if (mObservers != null)
            return ;
        mObservers = new ArrayMap<>();
        Stack stack = new Stack();
        stack.push(mPath);

        while (!stack.isEmpty())
        {
            String temp = (String) stack.pop();
            mObservers.put(temp, new SingleFileObserver(temp, mMask));
            File path = new File(temp);
            File[] files = path.listFiles();
            if (null == files)
                continue;
            for (File f: files)
            {
                if (f.isDirectory() && !f.getName().equals(".") && !f.getName()
                        .equals(".."))
                {
                    stack.push(f.getAbsolutePath());
                }
            }
        }

        Iterator<String> iterator = mObservers.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            mObservers.get(key).startWatching();
        }
        Logging.error("observers setting end");
    }

    @Override public void stopWatching()
    {
        if (mObservers == null)
            return ;

        Iterator<String> iterator = mObservers.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            mObservers.get(key).stopWatching();
        }
        mObservers.clear();
        mObservers = null;
    }

    public void onFileCreate(String path){

    }

    @Override public void onEvent(int event, String path)
    {
        int el = event & FileObserver.ALL_EVENTS;
        if(el == FileObserver.CREATE){
            File file = new File(path);
            onFileCreate(path);
            if(file.isDirectory()) {
                Stack stack = new Stack();
                stack.push(path);
                while (!stack.isEmpty())
                {
                    String temp = (String) stack.pop();
                    if(mObservers.containsKey(temp)) {
                        continue;
                    } else {
                        SingleFileObserver sfo = new SingleFileObserver(temp, mMask);
                        sfo.startWatching();
                        mObservers.put(temp, sfo);
                    }
                    File tempPath = new File(temp);
                    File[] files = tempPath.listFiles();
                    if (null == files)
                        continue;
                    for (File f: files)
                    {
                        if (f.isDirectory() && !f.getName().equals(".") && !f.getName()
                                .equals(".."))
                        {
                            stack.push(f.getAbsolutePath());
                        }
                    }
                }
            }
        }

    }

    class SingleFileObserver extends FileObserver
    {
        String mPath;

        public SingleFileObserver(String path) {
            this(path, ALL_EVENTS);
            mPath = path;
        }

        public SingleFileObserver(String path, int mask)
        {
            super(path, mask);
            mPath = path;
        }

        @Override public void onEvent(int event, String path)
        {
            if(path != null) {
                String newPath = mPath + "/" + path;
                RecursiveFileObserver.this.onEvent(event, newPath);
            }
        }
    }
}

