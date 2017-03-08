package com.github.privacystreams.core.providers;

import com.github.privacystreams.core.SingleItemStream;

/**
 * A function that produces a single-item stream.
 */
public abstract class SingleItemStreamProvider extends StreamProvider<SingleItemStream> {

    protected final void init() {
        this.output = new SingleItemStream(this.getUQI(), this);
        super.init();
    }

}
