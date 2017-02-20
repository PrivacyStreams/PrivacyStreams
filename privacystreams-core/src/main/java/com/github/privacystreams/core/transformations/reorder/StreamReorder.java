package com.github.privacystreams.core.transformations.reorder;

import java.util.List;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2MTransformation;

/**
 * Created by yuanchun on 22/12/2016.
 * A sorter reorder the items in a stream
 */
abstract class StreamReorder extends M2MTransformation {
    @Override
    protected void applyInBackground(MultiItemStream input, MultiItemStream output) {
        if (!this.isCancelled() && !output.isClosed()) {
            List<Item> items = input.readAll();
            this.reorder(items);
            for (Item item : items) {
                output.write(item);
            }
        }
    }

    protected abstract void reorder(List<Item> item);
}
