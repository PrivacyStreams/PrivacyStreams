package com.github.privacystreams.commons.comparison;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 29/12/2016.
 * A helper class to access comparison-related functions
 */

public class Comparisons {
    /**
     * A function that checks if the value of a field is equal to a given value
     * @param field the name of the field to compare
     * @param valueToCompare the value to compare with
     * @param <TValue> the type of value
     * @return the function
     */
    public static <TValue> Function<Item, Boolean> eq(final String field, final TValue valueToCompare) {
        return new FieldEqualPredicate<>(FieldEqualPredicate.OPERATOR_EQ, field, valueToCompare);
    }

    /**
     * A function that checks if the value of a field is not equal to a given value
     * @param field the name of the field to compare
     * @param valueToCompare the value to compare with
     * @param <TValue> the type of value
     * @return the function
     */
    public static <TValue> Function<Item, Boolean> ne(final String field, final TValue valueToCompare) {
        return new FieldEqualPredicate<>(FieldEqualPredicate.OPERATOR_NE, field, valueToCompare);
    }

    /**
     * A function that checks if the value of a field is greater than a given value
     * @param field the name of the field to compare
     * @param valueToCompare the value to compare with
     * @param <TValue> the type of value
     * @return the function
     */
    public static <TValue extends Comparable<TValue>> Function<Item, Boolean> gt(final String field, final TValue valueToCompare) {
        return new FieldComparePredicate<>(FieldComparePredicate.OPERATOR_GT, field, valueToCompare);
    }

    /**
     * A function that checks if the value of a field is less than a given value
     * @param field the name of the field to compare
     * @param valueToCompare the value to compare with
     * @param <TValue> the type of value
     * @return the function
     */
    public static <TValue extends Comparable<TValue>> Function<Item, Boolean> lt(final String field, final TValue valueToCompare) {
        return new FieldComparePredicate<>(FieldComparePredicate.OPERATOR_LT, field, valueToCompare);
    }

    /**
     * A function that checks if the value of a field is greater than or equal to a given value
     * @param field the name of the field to compare
     * @param valueToCompare the value to compare with
     * @param <TValue> the type of value
     * @return the function
     */
    public static <TValue extends Comparable<TValue>> Function<Item, Boolean> gte(final String field, final TValue valueToCompare) {
        return new FieldComparePredicate<>(FieldComparePredicate.OPERATOR_GTE, field, valueToCompare);
    }

    /**
     * A function that checks if the value of a field is less than or equal to a given value
     * @param field the name of the field to compare
     * @param valueToCompare the value to compare with
     * @param <TValue> the type of value
     * @return the function
     */
    public static <TValue extends Comparable<TValue>> Function<Item, Boolean> lte(final String field, final TValue valueToCompare) {
        return new FieldComparePredicate<>(FieldComparePredicate.OPERATOR_LTE, field, valueToCompare);
    }
}
