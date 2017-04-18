package com.github.privacystreams.commons.arithmetic;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access arithmetic operators.
 */
@PSOperatorWrapper
public class ArithmeticOperators {
    /**
     * Round up a number.
     * For example, given an item with field "x"=17, `roundUp("x", 10)` will produce 20.
     *
     * @param numField the name of the number field
     * @param value the value to round
     * @return the function
     */
    public static Function<Item, Double> roundUp(final String numField, final Number value) {
        return new RoundUpFunction(numField, value);
    }

    /**
     * Round down a number.
     * For example, given an item with field "x"=0.027, roundUp("x", 0.01) will produce 0.02.
     *
     * @param numField the name of the number field
     * @param value the value to round
     * @return the function
     */
    public static Function<Item, Double> roundDown(final String numField, final Number value) {
        return new RoundDownFunction(numField, value);
    }

    /**
     * Cast a number to long type.
     *
     * @param numField the name of the number field
     * @return the function
     */
    public static Function<Item, Long> castToLong(final String numField) {
        return new CastToLongFunction(numField);
    }

    /**
     * Cast a number to integer type.
     *
     * @param numField the name of the number field
     * @return the function
     */
    public static Function<Item, Integer> castToInt(final String numField) {
        return new CastToIntFunction(numField);
    }

    /**
     * Add the values of two fields in an item.
     *
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> add(final String numField1, final String numField2) {
        return new AddFunction(numField1, numField2);
    }

    /**
     * Subtract the values of two fields in an item.
     *
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> sub(final String numField1, final String numField2) {
        return new SubtractFunction(numField1, numField2);
    }

    /**
     * Multiply the values of two fields in an item.
     *
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> multiply(final String numField1, final String numField2) {
        return new MultiplyFunction(numField1, numField2);
    }

    /**
     * Divide the values of two fields in an item.
     *
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> divide(final String numField1, final String numField2) {
        return new DivideFunction(numField1, numField2);
    }

    /**
     * Mode the values of two fields in an item.
     *
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> mode(final String numField1, final String numField2) {
        return new ModeFunction(numField1, numField2);
    }
}
