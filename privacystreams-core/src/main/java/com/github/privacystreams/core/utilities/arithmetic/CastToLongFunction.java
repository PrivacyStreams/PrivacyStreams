package com.github.privacystreams.core.utilities.arithmetic;

import java.util.List;

import com.github.privacystreams.core.utils.Assertions;

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
