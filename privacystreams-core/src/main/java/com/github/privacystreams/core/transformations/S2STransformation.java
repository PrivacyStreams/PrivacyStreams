package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.SingleItemStream;

/**
 * Transform a single-item stream to another single-item stream
 */

public abstract class S2STransformation extends StreamTransformation<SingleItemStream, SingleItemStream> {

    protected void init() {
        super.init();
        this.output = new SingleItemStream(this.getUQI(), input.getStreamProvider().compound(this));
    }

}
