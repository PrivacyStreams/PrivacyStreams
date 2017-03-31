package com.github.privacystreams.commons.logic;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

/**
 * Compute NOT result of a boolean function.
 */
class NotFunction extends LogicFunction {

    private Function<Item, Boolean> function;

    NotFunction(Function<Item, Boolean> function) {
        this.function = Assertions.notNull("function", function);
        this.addParameters(function);
    }

    @Override
    public Boolean apply(UQI uqi, Item input) {
        boolean result = this.function.apply(uqi, input);
        return !result;
    }
}
