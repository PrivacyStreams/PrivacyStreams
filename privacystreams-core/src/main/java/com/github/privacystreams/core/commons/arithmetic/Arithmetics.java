package com.github.privacystreams.core.commons.arithmetic;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 29/12/2016.
 * A helper class to access list-related functions
 */

public class Arithmetics {
    /**
     * A function that round up a number.
     * @param numField the name of the number field
     * @param value the value to round
     * @return the function
     */
    public static Function<Item, Double> roundUp(final String numField, final Number value) {
        return new RoundUpFunction(numField, value);
    }

    /**
     * A function that round down a number.
     * @param numField the name of the number field
     * @param value the value to round
     * @return the function
     */
    public static Function<Item, Double> roundDown(final String numField, final Number value) {
        return new RoundDownFunction(numField, value);
    }

    /**
     * A function that cast a number to long.
     * @param numField the name of the number field
     * @return the function
     */
    public static Function<Item, Long> castToLong(final String numField) {
        return new CastToLongFunction(numField);
    }

    /**
     * A function that cast a number to integer.
     * @param numField the name of the number field
     * @return the function
     */
    public static Function<Item, Integer> castToInt(final String numField) {
        return new CastToIntFunction(numField);
    }
}
