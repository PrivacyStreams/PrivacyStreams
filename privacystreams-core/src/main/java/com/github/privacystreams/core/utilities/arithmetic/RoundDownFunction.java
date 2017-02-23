package com.github.privacystreams.core.utilities.arithmetic;

import java.util.List;

import com.github.privacystreams.core.utils.Assertions;

/**
 * Created by yuanchun on 05/02/2017.
 * Round a number field.
 */

public class RoundDownFunction extends ArithmeticFunction<Double> {

    private final Number valueToRound;

    RoundDownFunction(String numField, Number valueToRound) {
        super(numField);
        this.valueToRound = Assertions.notNull("valueToRound", valueToRound);
        this.addParameters(valueToRound);
    }

    @Override
    protected Double processNum(Number number) {
        return Math.floor(number.doubleValue()/valueToRound.doubleValue()) * valueToRound.doubleValue();
    }

}
