package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.SingleItemStream;

/**
 * Transform a multi-item stream to a single-item stream
 */

public abstract class M2STransformation extends StreamTransformation<MultiItemStream, SingleItemStream> {

    protected void init() {
        super.init();
        this.output = new SingleItemStream(this.getUQI(), input.getStreamProvider().compound(this));
    }
}
