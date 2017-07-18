package io.github.privacystreams.commons.item;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

import static io.github.privacystreams.utils.Assertions.notNull;

/**
 * Set a value to a field in the item.
 */

class FieldValueSetter<TValue> extends ItemOperator<Item> {

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
