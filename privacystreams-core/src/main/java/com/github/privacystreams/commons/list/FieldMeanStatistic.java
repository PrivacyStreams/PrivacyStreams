package com.github.privacystreams.commons.list;

import com.github.privacystreams.utils.StatisticUtils;

import java.util.List;

/**
 * Calculate the average of a field in the stream.
 * If calculation fails (e.g. there is no item in the stream), the "average" result will be null
 */
final class FieldMeanStatistic extends NumListProcessor<Double> {

    FieldMeanStatistic(String numListField) {
        super(numListField);
    }

    @Override
    protected Double processNumList(List<Number> numList) {
        return StatisticUtils.mean(numList);
    }
}
