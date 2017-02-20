package com.github.privacystreams.core.actions.callback;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 28/12/2016.
 * Callback with each item in the stream.
 * The callback will be invoked with the item map as a parameter.
 */
class ForEachCallback extends AsyncMultiItemStreamAction<Void> {
    private final Function<Item, Void> itemMapCallback;

    ForEachCallback(Function<Item, Void> itemMapCallback) {
        this.itemMapCallback = itemMapCallback;
    }

    @Override
    protected Void initOutput(MultiItemStream input) {
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

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(itemMapCallback);
        return parameters;
    }
}
