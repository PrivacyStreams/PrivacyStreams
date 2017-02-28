package com.github.privacystreams.core.transformations.reorder;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2MTransformation;

/**
 * Created by yuanchun on 22/12/2016.
 * A sorter reorder the items in a stream
 */
abstract class StreamReorder extends M2MTransformation {
    protected abstract void reorder(List<Item> item);

    private transient List<Item> items;
    @Override
    protected void onInput(Item item) {
        if (this.items == null) this.items = new ArrayList<>();
        if (item.isEndOfStream()) {
            this.reorder(items);
        }
        else {
            this.items.add(item);
        }
    }
}
