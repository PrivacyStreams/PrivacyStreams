package com.github.privacystreams.commons.list;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.Arrays;

/**
 * A helper class to access list-related functions.
 */
@PSOperatorWrapper
public class ListOperators {
    /**
     * Check whether the list specified by a field contains a given value.
     *
     * @param listField the name of the list field
     * @param value the value to check if is in the list
     * @return the predicate
     */
    public static Function<Item, Boolean> contains(final String listField, final Object value) {
        return new ListContainsPredicate(listField, value);
    }

    /**
     * Check whether the list specified by a field intersects with a given list.
     *
     * @param listField the name of the list field
     * @param listToCompare the list to compare
     * @return the predicate
     */
    public static <TValue> Function<Item, Boolean> intersects(final String listField, final TValue[] listToCompare) {
        return new ListContainsPredicate(listField, Arrays.asList(listToCompare));
    }

    /**
     * Count the number of elements in the list specified by a field.
     *
     * @param listField the name of the field to count
     * @return the function.
     */
    public static Function<Item, Integer> count(String listField) {
        return new FieldCounter(listField);
    }

    /**
     * Calculate the range of the numbers specified by a field.
     * The list elements must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no number in the list), the result will be null.
     *
     * @param numListField the name of the field, the field value must be a list of numbers.
     * @return the function
     */
    public static Function<Item, Double> range(String numListField) {
        return new FieldRangeStatistic(numListField);
    }

    /**
     * Calculate the sum of the numbers specified by a field.
     * The list elements must be in Number type, such as Integer, Double, Long, ...
     * If there is no valid field value in the stream, the "sum" result will be 0.0
     *
     * @param numListField the name of the field, the field value must be a list of numbers.
     * @return the function
     */
    public static Function<Item, Double> sum(String numListField) {
        return new FieldSumStatistic(numListField);
    }

    /**
     * Calculate the average of the numbers specified by a field.
     * The list elements must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no number in the list), the result will be null.
     *
     * @param numListField the name of the field, the field value must be a list of numbers.
     * @return the function
     */
    public static Function<Item, Double> mean(String numListField) {
        return new FieldMeanStatistic(numListField);
    }

    /**
     * Calculate the RMS (root mean square) of the numbers specified by a field.
     * The list elements must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no number in the list), the result will be null.
     *
     * @param numListField the name of the field, the field value must be a list of numbers.
     * @return the function
     */
    public static Function<Item, Double> rms(String numListField) {
        return new FieldRMSStatistic(numListField);
    }

    /**
     * Calculate the variance of the numbers specified by a field.
     * The list elements must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no number in the list), the result will be null.
     *
     * @param numListField the name of the field, the field value must be a list of numbers.
     * @return the function
     */
    public static Function<Item, Double> variance(String numListField) {
        return new FieldVarianceStatistic(numListField);
    }

    /**
     * Get the max value of a field in the stream.
     * The list elements must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no number in the list), the result will be null.
     *
     * @param numListField the name of the field, the field value must be a list of numbers.
     * @return the function
     */
    public static Function<Item, Number> max(String numListField) {
        return new FieldMaxStatistic(numListField);
    }

    /**
     * Get the min value of a field in the stream.
     * The list elements must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no number in the list), the result will be null.
     *
     * @param numListField the name of the field, the field value must be a list of numbers.
     * @return the function
     */
    public static Function<Item, Number> min(String numListField) {
        return new FieldMinStatistic(numListField);
    }

    /**
     * Get the median value of a field in the stream.
     * The list elements must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no number in the list), the result will be null.
     *
     * @param numListField the name of the field, the field value must be a list of numbers.
     * @return the function
     */
    public static Function<Item, Number> median(String numListField) {
        return new FieldMedianStatistic(numListField);
    }

    /**
     * Get the mode value of a field in the stream.
     * The list elements must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no number in the list), the result will be null.
     *
     * @param numListField the name of the field, the field value must be a list of numbers.
     * @return the function
     */
    public static Function<Item, Number> mode(String numListField) {
        return new FieldModeStatistic(numListField);
    }
}
