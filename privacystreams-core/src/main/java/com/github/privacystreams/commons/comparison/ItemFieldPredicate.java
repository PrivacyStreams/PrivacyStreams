package com.github.privacystreams.commons.comparison;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;

import static com.github.privacystreams.utils.Assertions.notNull;

/**
 * Make comparisons on field values.
 */
abstract class ItemFieldPredicate<TValue> extends ItemFunction<Boolean> {
    protected final String operator;
    protected final String field;
    protected final TValue valueToCompare;

    ItemFieldPredicate(final String operator, final String field, final TValue valueToCompare) {
        this.operator = notNull("operator", operator);
        this.field = notNull("field", field);
        this.valueToCompare = valueToCompare;
        this.addParameters(operator, field, valueToCompare);
    }

    public boolean test(Item item) {
        if (item == null) return false;
        TValue fieldValue = item.getValueByField(this.field);
        return testField(fieldValue);
    }

    protected abstract boolean testField(TValue fieldValue);

    public final Boolean apply(UQI uqi, Item input) {
        return this.test(input);
    }
}
