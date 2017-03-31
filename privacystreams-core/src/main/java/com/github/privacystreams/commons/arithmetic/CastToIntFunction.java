package com.github.privacystreams.commons.arithmetic;

/**
 * Round the number specified by a field.
 */
class CastToIntFunction extends ArithmeticFunction<Integer> {

    CastToIntFunction(String numField) {
        super(numField);
    }

    @Override
    protected Integer processNum(Number number) {
        return number.intValue();
    }

}
