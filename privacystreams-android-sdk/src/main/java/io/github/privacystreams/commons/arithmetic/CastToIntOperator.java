package io.github.privacystreams.commons.arithmetic;

/**
 * Round the number specified by a field.
 */
class CastToIntOperator extends ArithmeticOperator<Integer> {

    CastToIntOperator(String numField) {
        super(numField);
    }

    @Override
    protected Integer processNum(Number number) {
        return number.intValue();
    }

}
