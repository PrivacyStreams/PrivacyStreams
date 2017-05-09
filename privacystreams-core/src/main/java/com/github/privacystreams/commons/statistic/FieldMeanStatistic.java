package com.github.privacystreams.commons.statistic;

import com.github.privacystreams.utils.StatisticUtils;

import java.util.List;

/**
 * Calculate the average of a field in the stream.
 * If calculation fails (e.g. there is no item in the stream), the "average" result will be null
 */
final class FieldMeanStatistic extends FieldStatistic<Double> {

    FieldMeanStatistic(String numField) {
        super(numField);
    }

    @Override
    protected Double processNumList(List<Number> numList) {
        return StatisticUtils.mean(numList);
    }
}
