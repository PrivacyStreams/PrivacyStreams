package io.github.privacystreams.commons.comparison;

/**
 * Make numeric comparisons on field values
 */
final class FieldCompareOperator extends ItemFieldOperator<Number> {
    final static String OPERATOR_GT = "$field_gt";
    final static String OPERATOR_LT = "$field_lt";
    final static String OPERATOR_GTE = "$field_gte";
    final static String OPERATOR_LTE = "$field_lte";

    FieldCompareOperator(final String operator, final String field, final Number valueToCompare) {
        super(operator, field, valueToCompare);
    }

    @Override
    protected boolean testField(Number fieldValue) {
        if (fieldValue == null) return false;
        double compareResult = fieldValue.doubleValue() - this.valueToCompare.doubleValue();
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
