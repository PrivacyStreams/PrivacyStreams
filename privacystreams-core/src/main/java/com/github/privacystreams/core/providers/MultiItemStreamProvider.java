package com.github.privacystreams.core.providers;

import com.github.privacystreams.core.MultiItemStream;

/**
 * A function that produces a multi-item stream.
 */

public abstract class MultiItemStreamProvider extends StreamProvider<MultiItemStream> {

    protected final void init() {
        this.output = new MultiItemStream(this.getUQI(), this);
        super.init();
    }


}
