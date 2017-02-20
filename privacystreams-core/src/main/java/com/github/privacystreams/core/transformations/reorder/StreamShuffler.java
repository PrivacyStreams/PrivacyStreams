package com.github.privacystreams.core.transformations.reorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.privacystreams.core.Item;

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

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        return parameters;
    }
}
