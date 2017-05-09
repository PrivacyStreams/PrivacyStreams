package com.github.privacystreams.commons.comparison;

/**
 * Make equality-related comparisons on field values.
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
                return equals(fieldValue, this.valueToCompare);
            case OPERATOR_NE:
                return !equals(fieldValue, this.valueToCompare);
            default:
                throw new IllegalArgumentException("illegal operator: " + this.operator);
        }
    }

    private static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }
}
