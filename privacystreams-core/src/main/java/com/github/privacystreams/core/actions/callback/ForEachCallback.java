package com.github.privacystreams.core.actions.callback;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.actions.MStreamAction;
import com.github.privacystreams.utils.Assertions;

/**
 * Callback with each item in the stream.
 * The callback will be invoked with the item map as a parameter.
 */
class ForEachCallback extends MStreamAction {
    private final Function<Item, Void> itemCallback;

    ForEachCallback(Function<Item, Void> itemCallback) {
        this.itemCallback = Assertions.notNull("itemCallback", itemCallback);
        this.addParameters(itemCallback);
    }

    @Override
    protected void onInput(Item item) {
        if (item.isEndOfStream()) {
            this.finish();
            return;
        }
        this.itemCallback.apply(this.getUQI(), item);
    }
}
