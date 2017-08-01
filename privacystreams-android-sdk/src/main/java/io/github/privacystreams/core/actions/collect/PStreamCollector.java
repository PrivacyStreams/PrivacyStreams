package io.github.privacystreams.core.actions.collect;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamAction;
import io.github.privacystreams.utils.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * A function that outputs a multi-item stream.
 */

class PStreamCollector<Tout> extends PStreamAction {

    private Function<List<Item>, Tout> itemsCollector;
    private Function<Tout, Void> resultHandler;

    PStreamCollector(Function<List<Item>, Tout> itemsCollector, Function<Tout, Void> resultHandler) {
        this.itemsCollector = Assertions.notNull("itemsCollector", itemsCollector);
        this.resultHandler = resultHandler;
        this.addParameters(itemsCollector, resultHandler);
    }

    private transient List<Item> items;
    @Override
    protected void onInput(Item item) {
        if (this.items == null) this.items = new ArrayList<>();
        if (item.isEndOfStream()) {
            Tout result = itemsCollector.apply(this.getUQI(), this.items);
            if (this.resultHandler != null)
                this.resultHandler.apply(this.getUQI(), result);
            this.finish();
        }
        else {
            this.items.add(item);
        }
    }
}
