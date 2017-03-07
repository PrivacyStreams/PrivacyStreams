package com.github.privacystreams.core.commons.arithmetic;

/**
 * A function that divide two numbers.
 * Created by yuanchun on 07/03/2017.
 */

class DivideFunction extends Arithmetic2OpFunction<Number> {
    DivideFunction(String numField1, String numField2) {
        super(numField1, numField2);
    }

    @Override
    protected Number processNums(Number number1, Number number2) {
        return number1.doubleValue() / number2.doubleValue();
    }
}
