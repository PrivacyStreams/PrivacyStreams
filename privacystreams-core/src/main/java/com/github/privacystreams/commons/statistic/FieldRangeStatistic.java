package com.github.privacystreams.commons.statistic;

import com.github.privacystreams.utils.StatisticUtils;

import java.util.List;

/**
 * Calculate the range of a field in the stream.
 * The field value must be in Number type.
 * If calculation fails (e.g. there is no item in the stream), the "range" result will be null
 */
final class FieldRangeStatistic extends FieldStatistic<Double> {

    FieldRangeStatistic(String numField) {
        super(numField);
    }

    @Override
    protected Double processNumList(List<Number> numList) {
        return StatisticUtils.range(numList);
    }

}
