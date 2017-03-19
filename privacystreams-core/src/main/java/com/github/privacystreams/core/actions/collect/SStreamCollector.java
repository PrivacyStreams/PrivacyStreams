package com.github.privacystreams.core.actions.collect;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.actions.SStreamAction;
import com.github.privacystreams.utils.Assertions;

/**
 * A function that outputs a single-item stream.
 */

class SStreamCollector<Tout> extends SStreamAction {

    private Function<Item, Tout> itemCollector;
    private Function<Tout, Void> resultHandler;

    SStreamCollector(Function<Item, Tout> itemCollector, Function<Tout, Void> resultHandler) {
        this.itemCollector = Assertions.notNull("itemCollector", itemCollector);
        this.resultHandler = resultHandler;
        this.addParameters(itemCollector, resultHandler);
    }

    @Override
    protected void onInput(Item item) {
        if (item.isEndOfStream()) {
            this.finish();
            return;
        }
        Tout result = itemCollector.apply(this.getUQI(), item);
        if (this.resultHandler != null)
            this.resultHandler.apply(this.getUQI(), result);
    }
}
