package com.github.privacystreams.core.transformations.reorder;

import com.github.privacystreams.core.Item;

import java.util.Collections;
import java.util.List;

/**
 * Created by yuanchun on 22/12/2016.
 * Shuffle the items in the stream
 */
class StreamReverser extends StreamReorder {
    @Override
    protected void reorder(List<Item> items) {
        Collections.reverse(items);
    }
}
