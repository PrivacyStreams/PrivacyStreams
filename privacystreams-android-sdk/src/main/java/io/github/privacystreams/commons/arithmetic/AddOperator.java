package io.github.privacystreams.commons.arithmetic;

/**
 * Add two numbers
 */

class AddOperator extends Arithmetic2OpOperator<Number> {
    AddOperator(String numField1, String numField2) {
        super(numField1, numField2);
    }

    @Override
    protected Number processNums(Number number1, Number number2) {
        return number1.doubleValue() + number2.doubleValue();
    }
}
