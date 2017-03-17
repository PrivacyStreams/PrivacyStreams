package com.github.privacystreams.commons.item;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;

import static com.github.privacystreams.utils.Assertions.notNull;

/**
 * A function that gets the value of a field
 */

class FieldValueGetter<TValue> extends ItemFunction<TValue> {
    private final String fieldToGet;

    FieldValueGetter(String fieldToGet) {
        this.fieldToGet = notNull("fieldToGet", fieldToGet);
        this.addParameters(fieldToGet);
    }

    @Override
    public TValue apply(UQI uqi, Item input) {
        return input.getValueByField(this.fieldToGet);
    }
}
