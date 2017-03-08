package com.github.privacystreams.commons.arithmetic;

import com.github.privacystreams.core.utils.Assertions;

/**
 * Created by yuanchun on 05/02/2017.
 * Round a number field.
 */
class RoundUpFunction extends ArithmeticFunction<Double> {

    private final Number valueToRound;

    RoundUpFunction(String numField, Number valueToRound) {
        super(numField);
        this.valueToRound = Assertions.notNull("valueToRound", valueToRound);
        this.addParameters(valueToRound);
    }

    @Override
    protected Double processNum(Number number) {
        return Math.ceil(number.doubleValue()/valueToRound.doubleValue()) * valueToRound.doubleValue();
    }

}
