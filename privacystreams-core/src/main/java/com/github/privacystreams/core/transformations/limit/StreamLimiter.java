package com.github.privacystreams.core.transformations.limit;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2MTransformation;

/**
 * Created by yuanchun on 28/11/2016.
 * a StreamLimiter limit the length of MultiItemStream
 */
abstract class StreamLimiter extends M2MTransformation {

    @Override
    protected void applyInBackground(MultiItemStream input, MultiItemStream output) {
        while (!this.isCancelled() && !output.isClosed()) {
            Item item = input.read();
            if (item == null) break;
            if (this.keep(item)) output.write(item);
            else break;
        }
    }

    protected abstract boolean keep(Item item);
}
