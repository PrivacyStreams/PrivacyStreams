package com.github.privacystreams.core.transformations.reorder;

import com.github.privacystreams.core.Item;

import java.util.Collections;
import java.util.List;

/**
 * Shuffle the items in stream.
 */
final class StreamShuffler extends StreamReorder {
    StreamShuffler() {
    }

    @Override
    protected void reorder(List<Item> items) {
        Collections.shuffle(items);
    }
}
