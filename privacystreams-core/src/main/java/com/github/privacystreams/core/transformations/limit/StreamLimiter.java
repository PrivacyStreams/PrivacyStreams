package com.github.privacystreams.core.transformations.limit;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2MTransformation;

/**
 * Created by yuanchun on 28/11/2016.
 * a StreamLimiter limit the length of MultiItemStream
 */
abstract class StreamLimiter extends M2MTransformation {

    @Override
    protected void onInput(Item item) {
        if (item.isEndOfStream()) {
            this.finish();
            return;
        }
        if (this.keep(item)) this.output(item);
        else this.finish();
    }

    protected abstract boolean keep(Item item);
}
