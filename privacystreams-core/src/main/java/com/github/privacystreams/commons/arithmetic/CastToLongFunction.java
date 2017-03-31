package com.github.privacystreams.commons.arithmetic;

/**
 * Round the number specified by a field.
 */
class CastToLongFunction extends ArithmeticFunction<Long> {

    CastToLongFunction(String numField) {
        super(numField);
    }

    @Override
    protected Long processNum(Number number) {
        return number.longValue();
    }
}
