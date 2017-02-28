package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public abstract class M2MTransformation extends StreamTransformation<MultiItemStream, MultiItemStream> {
    @Override
    protected void init() {
        super.init();
        this.output = new MultiItemStream(this.getUQI(), input.getStreamProvider().compound(this));
    }
}
