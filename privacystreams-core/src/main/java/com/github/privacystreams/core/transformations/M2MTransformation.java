package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.MStream;

/**
 * Transform a multi-item stream to another multi-item stream
 */

public abstract class M2MTransformation extends StreamTransformation<MStream, MStream> {
    @Override
    protected void init() {
        this.output = new MStream(this.getUQI(), input.getStreamProvider().compound(this));
        super.init();
    }
}
