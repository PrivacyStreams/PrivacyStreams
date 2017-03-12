package com.github.privacystreams.core.transformations.reorder;

import com.github.privacystreams.core.Item;

import java.util.Collections;
import java.util.List;

/**
 * Created by yuanchun on 22/12/2016.
 * A function that shuffles the items in stream.
 */
final class StreamShuffler extends StreamReorder {
    StreamShuffler() {
    }

    @Override
    protected void reorder(List<Item> items) {
        Collections.shuffle(items);
    }
}
