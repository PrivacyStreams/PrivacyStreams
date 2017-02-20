package com.github.privacystreams.core.actions.callback;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 28/12/2016.
 * Callback once an item is present in the stream
 */

class IfPresentCallback extends AsyncMultiItemStreamAction<Void> {
    private final Function<Item, Void> callback;

    IfPresentCallback(Function<Item, Void> callback) {
        this.callback = callback;
    }

    @Override
    protected Void initOutput(MultiItemStream input) {
        return null;
    }

    @Override
    protected void applyInBackground(MultiItemStream input, Void output) {
        if (!this.isCancelled()) {
            Item item = input.read();
            if (item == null) return;
            this.callback.apply(this.getUQI(), item);
        }
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(callback);
        return parameters;
    }
}
