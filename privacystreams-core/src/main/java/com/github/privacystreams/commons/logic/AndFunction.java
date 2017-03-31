package com.github.privacystreams.commons.logic;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

/**
 * Compute AND result of two boolean functions.
 */
class AndFunction extends LogicFunction {

    private Function<Item, Boolean> function1;
    private Function<Item, Boolean> function2;

    AndFunction(Function<Item, Boolean> function1, Function<Item, Boolean> function2) {
        this.function1 = Assertions.notNull("function1", function1);
        this.function2 = Assertions.notNull("function2", function2);
        this.addParameters(function1, function2);
    }

    @Override
    public Boolean apply(UQI uqi, Item input) {
        boolean result1 = this.function1.apply(uqi, input);
        boolean result2 = this.function2.apply(uqi, input);
        return result1 && result2;
    }
}
