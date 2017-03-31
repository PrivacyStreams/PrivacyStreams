package com.github.privacystreams.core.transformations.select;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2STransformation;


/**
 * Pick an item from the stream.
 * Return null if it fails to find an item.
 */

class StreamItemPicker extends M2STransformation {
    private final int itemIndex;

    StreamItemPicker(int itemIndex) {
        this.itemIndex = itemIndex;
        this.addParameters(itemIndex);
    }

    private transient int itemCount = 0;
    @Override
    protected final void onInput(Item item) {
        if (item.isEndOfStream()) {
            this.finish();
            return;
        }
        if (itemCount == this.itemIndex) {
            this.output(item);
            this.finish();
            return;
        }
        itemCount++;
    }
}
