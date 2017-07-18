package com.github.privacystreams.commons.item;

import com.github.privacystreams.commons.ItemOperator;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

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
