package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.MStream;
import com.github.privacystreams.core.SStream;

/**
 * Transform a multi-item stream to a single-item stream
 */

public abstract class M2STransformation extends StreamTransformation<MStream, SStream> {

    protected void init() {
        this.output = new SStream(this.getUQI(), input.getStreamProvider().compound(this));
        super.init();
    }
}
