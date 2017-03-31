package com.github.privacystreams.commons.string;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

/**
 * Process the string specified by a field in an item.
 */
abstract class StringProcessor<Tout> extends ItemFunction<Tout> {

    private final String stringField;

    StringProcessor(String stringField) {
        this.stringField = Assertions.notNull("stringField", stringField);
        this.addParameters(stringField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        String stringValue = input.getValueByField(this.stringField);
        return this.processString(stringValue);
    }

    protected abstract Tout processString(String stringValue);

}
