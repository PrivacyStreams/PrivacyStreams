package io.github.privacystreams.commons.arithmetic;

/**
 * Round the number specified by a field.
 */
class CastToLongOperator extends ArithmeticOperator<Long> {

    CastToLongOperator(String numField) {
        super(numField);
    }

    @Override
    protected Long processNum(Number number) {
        return number.longValue();
    }
}
