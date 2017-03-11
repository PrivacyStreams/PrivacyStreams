package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.SingleItemStream;

/**
 * Transform a single-item stream to a multi-item stream
 */

public abstract class S2MTransformation extends StreamTransformation<SingleItemStream, MultiItemStream> {

    protected void init() {
        super.init();
        this.output = new MultiItemStream(this.getUQI(), input.getStreamProvider().compound(this));
    }
}
