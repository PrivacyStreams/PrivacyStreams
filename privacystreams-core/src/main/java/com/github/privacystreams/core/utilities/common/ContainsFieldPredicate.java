package com.github.privacystreams.core.utilities.common;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.utilities.ItemFunction;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 28/12/2016.
 * A predicate that returns true if the item contains a certain field
 */

class ContainsFieldPredicate extends ItemFunction<Boolean> {
    private final String fieldToCheck;

    ContainsFieldPredicate(String fieldToCheck) {
        this.fieldToCheck = fieldToCheck;
    }

    @Override
    public Boolean apply(UQI uqi, Item input) {
        return input.containsField(fieldToCheck);
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(fieldToCheck);
        return parameters;
    }
}
