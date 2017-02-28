package com.github.privacystreams.core.providers;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.UQI;

/**
 * Created by yuanchun on 29/11/2016.
 * Create a new stream of data
 */

public abstract class MultiItemStreamProvider extends StreamProvider<MultiItemStream> {

    protected final void init() {
        this.output = new MultiItemStream(this.getUQI(), this);
        super.init();
    }




    protected abstract void provide();

}
