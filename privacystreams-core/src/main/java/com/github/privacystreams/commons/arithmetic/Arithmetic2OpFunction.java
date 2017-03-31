package com.github.privacystreams.commons.arithmetic;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

/**
 * Process the number specified by a field in an item.
 */
abstract class Arithmetic2OpFunction<Tout> extends ItemFunction<Tout> {

    private final String numField1;
    private final String numField2;

    Arithmetic2OpFunction(String numField1, String numField2) {
        this.numField1 = Assertions.notNull("numField1", numField1);
        this.numField2 = Assertions.notNull("numField2", numField2);
        this.addParameters(numField1, numField2);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        Number number1 = input.getValueByField(this.numField1);
        Number number2 = input.getValueByField(this.numField2);
        return this.processNums(number1, number2);
    }

    protected abstract Tout processNums(Number number1, Number number2);
}
