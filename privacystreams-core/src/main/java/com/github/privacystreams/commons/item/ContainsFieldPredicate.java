package com.github.privacystreams.commons.item;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.utils.Assertions;

/**
 * Created by yuanchun on 28/12/2016.
 * A predicate that returns true if the item contains a certain field
 */

class ContainsFieldPredicate extends ItemFunction<Boolean> {
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
