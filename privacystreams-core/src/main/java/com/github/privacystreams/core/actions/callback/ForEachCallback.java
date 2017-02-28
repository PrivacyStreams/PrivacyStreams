package com.github.privacystreams.core.actions.callback;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.actions.StreamAction;
import com.github.privacystreams.core.utils.Assertions;

/**
 * Created by yuanchun on 28/12/2016.
 * Callback with each item in the stream.
 * The callback will be invoked with the item map as a parameter.
 */
class ForEachCallback extends StreamAction<MultiItemStream> {
    private final Function<Item, Void> itemCallback;

    ForEachCallback(Function<Item, Void> itemCallback) {
        this.itemCallback = Assertions.notNull("itemCallback", itemCallback);
        this.addParameters(itemCallback);
    }

    @Override
    protected void onInput(Item item) {
        if (item.isEndOfStream()) this.finish();
        this.itemCallback.apply(this.getUQI(), item);
    }
}
