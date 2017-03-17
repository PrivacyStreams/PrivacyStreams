package com.github.privacystreams.commons.comparison;

import org.apache.commons.lang3.ObjectUtils;

/**
 * Created by yuanchun on 28/11/2016.
 * a predicate that makes equality-related comparisons on field values
 */
final class FieldEqualPredicate<TValue> extends ItemFieldPredicate<TValue> {
    final static String OPERATOR_EQ = "$field_eq";
    final static String OPERATOR_NE = "$field_ne";

    FieldEqualPredicate(final String operator, final String field, final TValue valueToCompare) {
        super(operator, field, valueToCompare);
    }

    @Override
    protected boolean testField(TValue fieldValue) {
        switch (this.operator) {
            case OPERATOR_EQ:
                return ObjectUtils.equals(fieldValue, this.valueToCompare);
            case OPERATOR_NE:
                return !ObjectUtils.equals(fieldValue, this.valueToCompare);
            default:
                throw new IllegalArgumentException("illegal operator: " + this.operator);
        }
    }
}
