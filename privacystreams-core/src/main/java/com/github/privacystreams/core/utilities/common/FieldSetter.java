package com.github.privacystreams.core.utilities.common;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;

import static com.github.privacystreams.core.utils.Assertions.notNull;

/**
 * Created by yuanchun on 27/12/2016.
 * Set a value to a field in the item
 */

class FieldSetter<TValue> extends Function<Item, Item> {

    private final String fieldToSet;
    private final Function<Item, TValue> functionToComputeValue;

    FieldSetter(String fieldToSet, Function<Item, TValue> functionToComputeValue) {
        this.fieldToSet = notNull("fieldToSet", fieldToSet);
        this.functionToComputeValue = notNull("functionToComputeValue", functionToComputeValue);
    }

    @Override
    public Item apply(UQI uqi, Item input) {
        input.setFieldValue(this.fieldToSet, this.functionToComputeValue.apply(uqi, input));
        return input;
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(fieldToSet);
        parameters.add(functionToComputeValue.toString());
        return parameters;
    }

}
