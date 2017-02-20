package com.github.privacystreams.core.actions.callback;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;

/**
 * Created by yuanchun on 28/12/2016.
 * Callback with an item if the item is different from the former one.
 */

class OnChangeCallback extends AsyncMultiItemStreamAction<Void> {
    private final Function<Item, Void> callback;

    OnChangeCallback(Function<Item, Void> callback) {
        this.callback = callback;
    }

    @Override
    protected Void initOutput(MultiItemStream input) {
        return null;
    }

    @Override
    protected void applyInBackground(MultiItemStream input, Void output) {
        Item lastItem = null;
        while (!this.isCancelled()) {
            Item item = input.read();
            if (item == null) break;
            if (!item.equals(lastItem))
                this.callback.apply(this.getUQI(), item);
            lastItem = item;
        }
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(callback);
        return parameters;
    }
}
