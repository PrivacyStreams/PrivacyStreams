package com.github.privacystreams.commons.item;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;

import static com.github.privacystreams.utils.Assertions.notNull;

/**
 * Set a value to a field in the item.
 */

class FieldValueSetter<TValue> extends ItemFunction<Item> {

    private final String fieldToSet;
    private final TValue fieldValue;

    FieldValueSetter(String fieldToSet, TValue fieldValue) {
        this.fieldToSet = notNull("fieldToSet", fieldToSet);
        this.fieldValue = notNull("fieldValue", fieldValue);
        this.addParameters(fieldToSet, fieldValue);
    }

    @Override
    public Item apply(UQI uqi, Item input) {
        input.setFieldValue(this.fieldToSet, this.fieldValue);
        return input;
    }

}
