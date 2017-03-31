package com.github.privacystreams.commons.arithmetic;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

/**
 * Process the number field in an item.
 */
abstract class ArithmeticFunction<Tout> extends ItemFunction<Tout> {

    private final String numField;

    ArithmeticFunction(String numField) {
        this.numField = Assertions.notNull("numField", numField);
        this.addParameters(numField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        Number number = input.getValueByField(this.numField);
        return this.processNum(number);
    }

    protected abstract Tout processNum(Number number);
}
