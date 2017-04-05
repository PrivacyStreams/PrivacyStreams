package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.MStream;
import com.github.privacystreams.core.SStream;

/**
 * Transform a single-item stream to a multi-item stream
 */

public abstract class S2MTransformation extends StreamTransformation<SStream, MStream> {

    protected void init() {
        this.output = new MStream(this.getUQI(), input.getStreamProvider().compound(this));
        super.init();
    }
}
