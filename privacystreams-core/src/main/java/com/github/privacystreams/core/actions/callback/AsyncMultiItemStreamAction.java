package com.github.privacystreams.core.actions.callback;

import com.github.privacystreams.core.AsyncFunction;
import com.github.privacystreams.core.MultiItemStream;

/**
 * Created by yuanchun on 28/11/2016.
 * A stream collector collects the stream for output
 */

public abstract class AsyncMultiItemStreamAction<Tout> extends AsyncFunction<MultiItemStream, Tout> {

    protected void onStop(MultiItemStream input, Tout output) {
        input.close();
    }

}
