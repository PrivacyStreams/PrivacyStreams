package com.github.privacystreams.core.transformations.filter;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.transformations.M2MTransformation;

/**
 * Created by yuanchun on 28/11/2016.
 * A StreamFilter excludes some Items from MultiItemStream
 */

abstract class StreamFilter extends M2MTransformation {

    @Override
    protected void onInput(Item item) {
        if (this.keep(item)) this.output(item);
    }

    protected abstract boolean keep(Item item);
}
