package com.github.privacystreams.core.providers;

import com.github.privacystreams.core.PStream;

/**
 * A function that produces a privacy stream.
 */

public abstract class PStreamProvider extends StreamProvider<PStream> {

    protected final void init() {
        this.output = new PStream(this.getUQI(), this);
        super.init();
    }


}
