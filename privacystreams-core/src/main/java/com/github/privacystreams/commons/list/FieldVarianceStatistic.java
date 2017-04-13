package com.github.privacystreams.commons.list;

import com.github.privacystreams.utils.StatisticUtils;

import java.util.List;

/**
 * Calculate the variance of a field in the stream.
 * The field field must be in Number type.
 * If there is no valid field value in the stream, the "sum" result will be 0.0
 */
final class FieldVarianceStatistic extends NumListProcessor<Double> {

    FieldVarianceStatistic(String numListField) {
        super(numListField);
    }

    @Override
    protected Double processNumList(List<Number> numList) {
        return StatisticUtils.variance(numList);
    }
}
