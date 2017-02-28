package com.github.privacystreams.core.actions;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.SingleItemStream;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.utils.Assertions;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public class SingleItemStreamAction<Tout> extends StreamAction<SingleItemStream> {

    private Function<Item, Tout> itemOutputFunction;
    private Function<Tout, Void> resultHandler;

    public SingleItemStreamAction(Function<Item, Tout> itemOutputFunction, Function<Tout, Void> resultHandler) {
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
