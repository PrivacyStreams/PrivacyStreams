package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;

/**
 * Transform a multi-item stream to another multi-item stream
 */

public abstract class M2MTransformation extends StreamTransformation<MultiItemStream, MultiItemStream> {
    @Override
    protected void init() {
        super.init();
        this.output = new MultiItemStream(this.getUQI(), input.getStreamProvider().compound(this));
    }
}
