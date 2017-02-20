package com.github.privacystreams.core.utilities.string;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.utilities.ItemFunction;
import com.github.privacystreams.core.utils.Assertions;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 28/12/2016.
 * Process the string field in an item.
 */
abstract class StringProcessor<Tout> extends ItemFunction<Tout> {

    private final String stringField;

    StringProcessor(String stringField) {
        this.stringField = Assertions.notNull("stringField", stringField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        String stringValue = input.getValueByField(this.stringField);
        return this.processString(stringValue);
    }

    protected abstract Tout processString(String stringValue);

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(this.stringField);
        return parameters;
    }
}
