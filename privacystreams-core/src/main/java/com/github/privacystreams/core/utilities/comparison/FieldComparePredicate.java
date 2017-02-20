package com.github.privacystreams.core.utilities.comparison;

import com.github.privacystreams.core.utils.Assertions;

/**
 * Created by yuanchun on 28/11/2016.
 * a predicate that makes numeric comparisons on field values
 */
final class FieldComparePredicate<TValue extends Comparable<TValue>> extends ItemFieldPredicate<TValue> {
    final static String OPERATOR_GT = "$field_gt";
    final static String OPERATOR_LT = "$field_lt";
    final static String OPERATOR_GTE = "$field_gte";
    final static String OPERATOR_LTE = "$field_lte";

    FieldComparePredicate(final String operator, final String field, final TValue valueToCompare) {
        super(operator, field, valueToCompare);
    }

    @Override
    protected boolean testField(Object fieldValue) {
        if (fieldValue == null) return false;
        Comparable<TValue> fieldComparable = Assertions.cast("compared_field_value", fieldValue);
        int compareResult = fieldComparable.compareTo(this.valueToCompare);
        switch (this.operator) {
            case OPERATOR_GT:
                return compareResult > 0;
            case OPERATOR_LT:
                return compareResult < 0;
            case OPERATOR_GTE:
                return compareResult >= 0;
            case OPERATOR_LTE:
                return compareResult <= 0;
            default:
                throw new IllegalArgumentException("illegal operator: " + this.operator);
        }
    }
}
