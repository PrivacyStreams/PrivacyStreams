package io.github.privacystreams.commons.item;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

/**
 * A function that is independent from current item.
 */

class IndependentItemOperator<Tout> extends ItemOperator<Tout> {
    private Function<Void, Tout> voidInFunction;

    IndependentItemOperator(Function<Void, Tout> voidInFunction) {
        this.voidInFunction = Assertions.notNull("voidInFunction", voidInFunction);
        this.addParameters(voidInFunction);
    }

    @Override
    public Tout apply(UQI uqi, Item input) {
        return this.voidInFunction.apply(uqi, null);
    }
}
