package com.github.privacystreams.commons.statistic;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.List;

/**
 * A helper class to access statistic-related functions
 */
@PSOperatorWrapper
public class StatisticOperators {
    /**
     * Count the number of items in the stream and output the number.
     *
     * @return the function.
     */
    public static Function<List<Item>, Integer> count() {
        return new StreamCounter();
    }

    /**
     * Count the number of valid fields in the stream and output the number.
     *
     * @param field the name of the field to count
     * @return the function.
     */
    public static Function<List<Item>, Integer> count(String field) {
        return new FieldCounter(field);
    }

    /**
     * Calculate the range of the number values of a field in the stream.
     * The field values must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no item in the stream), the result will be null.
     *
     * @param numField the name of the field, the field value must be a number.
     * @return the function
     */
    public static Function<List<Item>, Double> range(String numField) {
        return new FieldRangeStatistic(numField);
    }

    /**
     * Calculate the sum of the number values of a field in the stream.
     * The field values must be in Number type, such as Integer, Double, Long, ...
     * If there is no valid field value in the stream, the "sum" result will be 0.0
     *
     * @param numField the name of the field, the field value must be a number.
     * @return the function
     */
    public static Function<List<Item>, Double> sum(String numField) {
        return new FieldSumStatistic(numField);
    }

    /**
     * Calculate the average of the number values of a field in the stream.
     * The field values must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no item in the stream), the result will be null.
     *
     * @param numField the name of the field, the field value must be a number.
     * @return the function
     */
    public static Function<List<Item>, Double> mean(String numField) {
        return new FieldMeanStatistic(numField);
    }

    /**
     * Calculate the RMS (root mean square) of the number values of a field in the stream.
     * The field values must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no item in the stream), the result will be null.
     *
     * @param numField the name of the field, the field value must be a number.
     * @return the function
     */
    public static Function<List<Item>, Double> rms(String numField) {
        return new FieldRMSStatistic(numField);
    }

    /**
     * Calculate the variance of the number values of a field in the stream.
     * The field values must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no item in the stream), the result will be null.
     *
     * @param numField the name of the field, the field value must be a number.
     * @return the function
     */
    public static Function<List<Item>, Double> variance(String numField) {
        return new FieldVarianceStatistic(numField);
    }

    /**
     * Get the max value of a field in the stream.
     * The field values must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no item in the stream), the result will be null.
     *
     * @param numField the name of the field, the field value must be a number.
     * @return the function
     */
    public static Function<List<Item>, Number> max(String numField) {
        return new FieldMaxStatistic(numField);
    }

    /**
     * Get the min value of a field in the stream.
     * The field values must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no item in the stream), the result will be null.
     *
     * @param numField the name of the field, the field value must be a number.
     * @return the function
     */
    public static Function<List<Item>, Number> min(String numField) {
        return new FieldMinStatistic(numField);
    }

    /**
     * Get the median value of a field in the stream.
     * The field values must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no item in the stream), the result will be null.
     *
     * @param numField the name of the field, the field value must be a number.
     * @return the function
     */
    public static Function<List<Item>, Number> median(String numField) {
        return new FieldMedianStatistic(numField);
    }

    /**
     * Get the mode value of a field in the stream.
     * The field values must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no item in the stream), the result will be null.
     *
     * @param numField the name of the field, the field value must be a number.
     * @return the function
     */
    public static Function<List<Item>, Number> mode(String numField) {
        return new FieldModeStatistic(numField);
    }
}
