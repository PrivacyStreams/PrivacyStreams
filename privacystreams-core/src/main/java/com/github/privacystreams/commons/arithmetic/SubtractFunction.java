package com.github.privacystreams.commons.arithmetic;

/**
 * Subtract two numbers.
 */

class SubtractFunction extends Arithmetic2OpFunction<Number> {
    SubtractFunction(String numField1, String numField2) {
        super(numField1, numField2);
    }

    @Override
    protected Number processNums(Number number1, Number number2) {
        return number1.doubleValue() - number2.doubleValue();
    }
}
