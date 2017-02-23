package com.github.privacystreams.core.actions;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.SingleItemStream;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.utils.Assertions;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public class SingleItemStreamAction<Tout> extends Function<SingleItemStream, Tout> {

    private Function<Item, Tout> itemOutputFunction;

    public SingleItemStreamAction(Function<Item, Tout> itemOutputFunction) {
        this.itemOutputFunction = Assertions.notNull("itemOutputFunction;", itemOutputFunction);
        this.addParameters(itemOutputFunction);
    }

    @Override
    public Tout apply(UQI uqi, SingleItemStream input) {
        return itemOutputFunction.apply(uqi, input.read());
    }

}
