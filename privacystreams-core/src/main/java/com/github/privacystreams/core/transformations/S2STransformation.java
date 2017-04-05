package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.SStream;

/**
 * Transform a single-item stream to another single-item stream
 */

public abstract class S2STransformation extends StreamTransformation<SStream, SStream> {

    protected void init() {
        this.output = new SStream(this.getUQI(), input.getStreamProvider().compound(this));
        super.init();
    }

}
