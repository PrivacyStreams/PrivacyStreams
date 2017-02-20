package com.github.privacystreams.core.actions;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.UQI;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public class MultiItemStreamAction<Tout> extends Function<MultiItemStream, Tout> {

    private Function<List<Item>, Tout> itemsOutputFunction;

    public MultiItemStreamAction(Function<List<Item>, Tout> itemsOutputFunction) {
        this.itemsOutputFunction = itemsOutputFunction;
    }

    @Override
    public Tout apply(UQI uqi, MultiItemStream input) {
        return itemsOutputFunction.apply(uqi, input.readAll());
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(this.itemsOutputFunction);
        return parameters;
    }
}
