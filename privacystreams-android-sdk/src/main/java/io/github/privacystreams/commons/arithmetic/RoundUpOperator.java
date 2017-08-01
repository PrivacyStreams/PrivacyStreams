package io.github.privacystreams.commons.arithmetic;

import io.github.privacystreams.utils.Assertions;

/**
 * Round up the number specified by a field.
 */
class RoundUpOperator extends ArithmeticOperator<Double> {

    private final Number valueToRound;

    RoundUpOperator(String numField, Number valueToRound) {
        super(numField);
        this.valueToRound = Assertions.notNull("valueToRound", valueToRound);
        this.addParameters(valueToRound);
    }

    @Override
    protected Double processNum(Number number) {
        return Math.ceil(number.doubleValue()/valueToRound.doubleValue()) * valueToRound.doubleValue();
    }

}
