package com.github.privacystreams.commons.statistic;

import com.github.privacystreams.utils.StatisticUtils;

import java.util.List;

/**
 * Calculate the sum of a field in the stream.
 * The field field must be in Number type.
 * If there is no valid field value in the stream, the "sum" result will be 0.0
 */
final class FieldSumStatistic extends FieldStatistic<Double> {

    FieldSumStatistic(String numField) {
        super(numField);
    }

    @Override
    protected Double processNumList(List<Number> numList) {
        return StatisticUtils.sum(numList);
    }
}
