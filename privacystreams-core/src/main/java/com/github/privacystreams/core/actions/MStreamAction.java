package com.github.privacystreams.core.actions;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MStream;
import com.github.privacystreams.utils.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * A function that outputs a multi-item stream.
 */

public class MStreamAction<Tout> extends StreamAction<MStream> {

    private Function<List<Item>, Tout> itemsOutputFunction;
    private Function<Tout, Void> resultHandler;

    public MStreamAction(Function<List<Item>, Tout> itemsOutputFunction, Function<Tout, Void> resultHandler) {
        this.itemsOutputFunction = Assertions.notNull("itemsOutputFunction", itemsOutputFunction);
        this.resultHandler = resultHandler;
        this.addParameters(itemsOutputFunction, resultHandler);
    }

    private transient List<Item> items;
    @Override
    protected void onInput(Item item) {
        if (this.items == null) this.items = new ArrayList<>();
        if (item.isEndOfStream()) {
            Tout result = itemsOutputFunction.apply(this.getUQI(), this.items);
            if (this.resultHandler != null)
                this.resultHandler.apply(this.getUQI(), result);
            this.finish();
        }
        else {
            this.items.add(item);
        }
    }
}
