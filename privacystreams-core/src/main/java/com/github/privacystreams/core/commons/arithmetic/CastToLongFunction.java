package com.github.privacystreams.core.commons.arithmetic;

/**
 * Created by yuanchun on 05/02/2017.
 * Round a number field.
 */

public class CastToLongFunction extends ArithmeticFunction<Long> {

    CastToLongFunction(String numField) {
        super(numField);
    }

    @Override
    protected Long processNum(Number number) {
        return number.longValue();
    }
}
