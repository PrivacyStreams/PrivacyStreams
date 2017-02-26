package com.github.privacystreams.core.actions;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.utils.Assertions;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public class MultiItemStreamAction<Tout> extends Function<MultiItemStream, Void> {

    private Function<List<Item>, Tout> itemsOutputFunction;
    private Function<Tout, Void> resultHandler;

    public MultiItemStreamAction(Function<List<Item>, Tout> itemsOutputFunction, Function<Tout, Void> resultHandler) {
        this.itemsOutputFunction = Assertions.notNull("itemsOutputFunction", itemsOutputFunction);
        this.resultHandler = resultHandler;
        this.addParameters(itemsOutputFunction, resultHandler);
    }

    @Override
    public Void apply(UQI uqi, MultiItemStream input) {
        Tout result = itemsOutputFunction.apply(uqi, input.readAll());
        if (this.resultHandler != null)
            this.resultHandler.apply(uqi, result);
        return null;
    }

}
