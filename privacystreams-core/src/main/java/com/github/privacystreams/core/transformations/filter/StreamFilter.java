package com.github.privacystreams.core.transformations.filter;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2MTransformation;

/**
 * Exclude some items from MStream
 */

abstract class StreamFilter extends M2MTransformation {

    @Override
    protected void onInput(Item item) {
        if (item.isEndOfStream()) {
            this.finish();
            return;
        }
        if (this.keep(item)) this.output(item);
    }

    protected abstract boolean keep(Item item);
}
