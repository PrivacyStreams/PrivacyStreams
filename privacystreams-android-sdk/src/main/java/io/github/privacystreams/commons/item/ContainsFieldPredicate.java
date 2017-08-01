package io.github.privacystreams.commons.item;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

/**
 * Check if the item contains a certain field
 */

class ContainsFieldPredicate extends ItemOperator<Boolean> {
    private final String fieldToCheck;

    ContainsFieldPredicate(String fieldToCheck) {
        this.fieldToCheck = Assertions.notNull("fieldToCheck", fieldToCheck);
        this.addParameters(fieldToCheck);
    }

    @Override
    public Boolean apply(UQI uqi, Item input) {
        return input.containsField(fieldToCheck);
    }
}
