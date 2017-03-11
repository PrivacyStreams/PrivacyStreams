package com.github.privacystreams.core.transformations.reorder;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2MTransformation;

/**
 * A function that reorders the items in a stream
 */
abstract class StreamReorder extends M2MTransformation {
    protected abstract void reorder(List<Item> item);

    private transient List<Item> items;

    @Override
    protected void onInput(Item item) {
        if (this.items == null) this.items = new ArrayList<>();
        if (item.isEndOfStream()) {
            this.reorder(items);
            return;
        }
        this.items.add(item);
    }
}
