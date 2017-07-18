package com.github.privacystreams.commons.arithmetic;

import com.github.privacystreams.commons.ItemOperator;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

/**
 * Process the number field in an item.
 */
abstract class ArithmeticOperator<Tout> extends ItemOperator<Tout> {

    private final String numField;

    ArithmeticOperator(String numField) {
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
