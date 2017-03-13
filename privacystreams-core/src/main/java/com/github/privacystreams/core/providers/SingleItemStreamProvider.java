package com.github.privacystreams.core.providers;

import com.github.privacystreams.core.SStream;

/**
 * A function that produces a single-item stream.
 */
public abstract class SingleItemStreamProvider extends StreamProvider<SStream> {

    protected final void init() {
        this.output = new SStream(this.getUQI(), this);
        super.init();
    }

}
