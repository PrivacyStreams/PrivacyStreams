package io.github.privacystreams.commons.string;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

/**
 * Process the string specified by a field in an item.
 */
abstract class StringProcessor<Tout> extends ItemOperator<Tout> {

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
