package com.github.privacystreams.core.actions;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.SStream;
import com.github.privacystreams.utils.Assertions;

/**
 * A function that outputs a single-item stream.
 */

public class SStreamAction<Tout> extends StreamAction<SStream> {

    private Function<Item, Tout> itemOutputFunction;
    private Function<Tout, Void> resultHandler;

    public SStreamAction(Function<Item, Tout> itemOutputFunction, Function<Tout, Void> resultHandler) {
        this.itemOutputFunction = Assertions.notNull("itemOutputFunction", itemOutputFunction);
        this.resultHandler = resultHandler;
        this.addParameters(itemOutputFunction, resultHandler);
    }

    @Override
    protected void onInput(Item item) {
        if (item.isEndOfStream()) this.finish();
        Tout result = itemOutputFunction.apply(this.getUQI(), item);
        if (this.resultHandler != null)
            this.resultHandler.apply(this.getUQI(), result);
    }
}
