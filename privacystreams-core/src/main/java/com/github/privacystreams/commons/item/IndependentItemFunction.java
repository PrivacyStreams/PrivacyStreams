package com.github.privacystreams.commons.item;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.transformations.group.GroupItem;
import com.github.privacystreams.utils.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * A function that is independent from current item.
 */

class IndependentItemFunction<Tout> extends ItemFunction<Tout> {
    private Function<Void, Tout> voidInFunction;

    IndependentItemFunction(Function<Void, Tout> voidInFunction) {
        this.voidInFunction = Assertions.notNull("voidInFunction", voidInFunction);
        this.addParameters(voidInFunction);
    }

    @Override
    public Tout apply(UQI uqi, Item input) {
        return this.voidInFunction.apply(uqi, null);
    }
}
