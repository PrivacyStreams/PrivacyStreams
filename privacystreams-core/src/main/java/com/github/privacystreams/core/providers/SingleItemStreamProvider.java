package com.github.privacystreams.core.providers;

import com.github.privacystreams.core.SingleItemStream;

/**
 * Created by yuanchun on 29/11/2016.
 * Create a new stream of data
 */

public abstract class SingleItemStreamProvider extends StreamProvider<SingleItemStream> {

    protected final void init() {
        super.init();
        this.output = new SingleItemStream(this.getUQI(), this);
    }

    protected abstract void provide();
}
