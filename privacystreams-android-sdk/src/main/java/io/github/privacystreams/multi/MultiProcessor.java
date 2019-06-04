package io.github.privacystreams.multi;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

abstract class MultiProcessor<Tout> extends ItemOperator<Tout> {

    @Override
    public final Tout apply(UQI uqi, Item input) {
        return this.processMulti(uqi, input);
    }

    protected abstract Tout processMulti(UQI uqi, Item item);
}

