package com.github.privacystreams.core.actions.callback;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.utils.Assertions;

/**
 * Created by yuanchun on 28/12/2016.
 * Callback with each item in the stream.
 * The callback will be invoked with the item map as a parameter.
 */
class ForEachCallback extends AsyncMultiItemStreamAction<Void> {
    private final Function<Item, Void> itemMapCallback;

    ForEachCallback(Function<Item, Void> itemMapCallback) {
        this.itemMapCallback = Assertions.notNull("itemMapCallback", itemMapCallback);
        this.addParameters(itemMapCallback);
    }

    @Override
    protected Void init(MultiItemStream input) {
        return null;
    }

    @Override
    protected void applyInBackground(MultiItemStream input, Void output) {
        while (!this.isCancelled()) {
            Item item = input.read();
            if (item == null) break;
            this.itemMapCallback.apply(this.getUQI(), item);
        }
    }
}
