package com.github.privacystreams.core.providers;

import com.github.privacystreams.core.MStream;

/**
 * A function that produces a multi-item stream.
 */

public abstract class MStreamProvider extends StreamProvider<MStream> {

    protected final void init() {
        this.output = new MStream(this.getUQI(), this);
        super.init();
    }


}
