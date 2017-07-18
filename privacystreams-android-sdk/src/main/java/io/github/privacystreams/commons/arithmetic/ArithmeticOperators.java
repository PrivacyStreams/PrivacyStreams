package io.github.privacystreams.commons.arithmetic;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

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
        return new RoundUpOperator(numField, value);
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
        return new RoundDownOperator(numField, value);
    }

    /**
     * Cast a number to long type.
     *
     * @param numField the name of the number field
     * @return the function
     */
    public static Function<Item, Long> castToLong(final String numField) {
        return new CastToLongOperator(numField);
    }

    /**
     * Cast a number to integer type.
     *
     * @param numField the name of the number field
     * @return the function
     */
    public static Function<Item, Integer> castToInt(final String numField) {
        return new CastToIntOperator(numField);
    }

    /**
     * Add the values of two fields in an item.
     *
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> add(final String numField1, final String numField2) {
        return new AddOperator(numField1, numField2);
    }

    /**
     * Subtract the values of two fields in an item.
     *
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> sub(final String numField1, final String numField2) {
        return new SubtractOperator(numField1, numField2);
    }

    /**
     * Multiply the values of two fields in an item.
     *
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> multiply(final String numField1, final String numField2) {
        return new MultiplyOperator(numField1, numField2);
    }

    /**
     * Divide the values of two fields in an item.
     *
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> divide(final String numField1, final String numField2) {
        return new DivideOperator(numField1, numField2);
    }

    /**
     * Mode the values of two fields in an item.
     *
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> mode(final String numField1, final String numField2) {
        return new ModeOperator(numField1, numField2);
    }
}
