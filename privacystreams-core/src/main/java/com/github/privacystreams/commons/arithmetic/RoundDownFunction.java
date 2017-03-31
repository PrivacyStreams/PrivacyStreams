package com.github.privacystreams.commons.arithmetic;

import com.github.privacystreams.utils.Assertions;

/**
 * Round the number specified by a field.
 */
class RoundDownFunction extends ArithmeticFunction<Double> {

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
