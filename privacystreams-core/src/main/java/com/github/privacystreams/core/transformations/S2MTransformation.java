package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.SingleItemStream;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public abstract class S2MTransformation extends StreamTransformation<SingleItemStream, MultiItemStream> {

    protected MultiItemStream init(SingleItemStream input) {
        return new MultiItemStream(this.getUQI(), input.getStreamProvider().compound(this));
    }
}
