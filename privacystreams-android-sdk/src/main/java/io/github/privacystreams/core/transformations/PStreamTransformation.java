package io.github.privacystreams.core.transformations;

import io.github.privacystreams.core.PStream;

/**
 * Transform a multi-item stream to another multi-item stream
 */

public abstract class PStreamTransformation extends StreamTransformation<PStream, PStream> {
    @Override
    protected void init() {
        this.output = new PStream(this.getUQI(), input.getStreamProvider().compound(this));
        super.init();
    }
}
