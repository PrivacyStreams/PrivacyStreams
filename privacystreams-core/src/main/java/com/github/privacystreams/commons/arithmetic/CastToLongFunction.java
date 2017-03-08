package com.github.privacystreams.commons.arithmetic;

/**
 * Created by yuanchun on 05/02/2017.
 * Round a number field.
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
