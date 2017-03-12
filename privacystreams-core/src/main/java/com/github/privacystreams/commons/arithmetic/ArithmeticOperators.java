package com.github.privacystreams.commons.arithmetic;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * Created by yuanchun on 29/12/2016.
 * A helper class to access list-related functions
 */
@PSOperatorWrapper
public class ArithmeticOperators {
    /**
     * A function that rounds up a number.
     * @param numField the name of the number field
     * @param value the value to round
     * @return the function
     */
    public static Function<Item, Double> roundUp(final String numField, final Number value) {
        return new RoundUpFunction(numField, value);
    }

    /**
     * A function that rounds down a number.
     * @param numField the name of the number field
     * @param value the value to round
     * @return the function
     */
    public static Function<Item, Double> roundDown(final String numField, final Number value) {
        return new RoundDownFunction(numField, value);
    }

    /**
     * A function that casts a number to long.
     * @param numField the name of the number field
     * @return the function
     */
    public static Function<Item, Long> castToLong(final String numField) {
        return new CastToLongFunction(numField);
    }

    /**
     * A function that casts a number to integer.
     * @param numField the name of the number field
     * @return the function
     */
    public static Function<Item, Integer> castToInt(final String numField) {
        return new CastToIntFunction(numField);
    }

    /**
     * A function that adds two fields in an item
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> add(final String numField1, final String numField2) {
        return new AddFunction(numField1, numField2);
    }

    /**
     * A function that subtracts two fields in an item
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> sub(final String numField1, final String numField2) {
        return new SubtractFunction(numField1, numField2);
    }

    /**
     * A function that multiplies two fields in an item
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> multiply(final String numField1, final String numField2) {
        return new MultiplyFunction(numField1, numField2);
    }

    /**
     * A function that divides two fields in an item
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> divide(final String numField1, final String numField2) {
        return new DivideFunction(numField1, numField2);
    }

    /**
     * A function that modes two fields in an item
     * @param numField1 the name of the first field
     * @param numField2 the name of the second field
     * @return the function
     */
    public static Function<Item, Number> mode(final String numField1, final String numField2) {
        return new ModeFunction(numField1, numField2);
    }
}
