package com.github.privacystreams.commons.item;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;

import static com.github.privacystreams.utils.Assertions.notNull;

/**
 * Set a value to a field in the item.
 */

class FieldSetter<TValue> extends ItemFunction<Item> {

    private final String fieldToSet;
    private final Function<Item, TValue> functionToComputeValue;

    FieldSetter(String fieldToSet, Function<Item, TValue> functionToComputeValue) {
        this.fieldToSet = notNull("fieldToSet", fieldToSet);
        this.functionToComputeValue = notNull("functionToComputeValue", functionToComputeValue);
        this.addParameters(fieldToSet, functionToComputeValue);
    }

    @Override
    public Item apply(UQI uqi, Item input) {
        input.setFieldValue(this.fieldToSet, this.functionToComputeValue.apply(uqi, input));
        return input;
    }

}
