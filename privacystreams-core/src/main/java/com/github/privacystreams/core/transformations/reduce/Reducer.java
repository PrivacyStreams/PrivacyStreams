package com.github.privacystreams.core.transformations.reduce;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2STransformation;


/**
 * Reduce two items to one item.
 */
public abstract class Reducer extends M2STransformation {
    protected abstract Item reduce(Item item1, Item item2);

    private transient Item reducedItem = null;
    @Override
    protected final void onInput(Item item) {
        if (item.isEndOfStream()) {
            this.output(reducedItem);
            this.finish();
            return;
        }
        this.reducedItem = this.reduce(this.reducedItem, item);
    }
}
