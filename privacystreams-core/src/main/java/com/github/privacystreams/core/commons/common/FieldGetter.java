package com.github.privacystreams.core.commons.common;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.commons.ItemFunction;

import static com.github.privacystreams.core.utils.Assertions.notNull;

/**
 * Created by yuanchun on 27/12/2016.
 * A function that gets the value of a field
 */

class FieldGetter<TValue> extends ItemFunction<TValue> {
    private final String fieldToGet;

    FieldGetter(String fieldToGet) {
        this.fieldToGet = notNull("fieldToGet", fieldToGet);
        this.addParameters(fieldToGet);
    }

    @Override
    public TValue apply(UQI uqi, Item input) {
        return input.getValueByField(this.fieldToGet);
    }
}
