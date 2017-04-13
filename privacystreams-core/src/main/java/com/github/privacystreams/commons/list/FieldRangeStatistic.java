package com.github.privacystreams.commons.list;

import com.github.privacystreams.utils.StatisticUtils;

import java.util.List;

/**
 * Calculate the range of a field in the stream.
 * The field value must be in Number type.
 * If calculation fails (e.g. there is no item in the stream), the "range" result will be null
 */
final class FieldRangeStatistic extends NumListProcessor<Double> {

    FieldRangeStatistic(String numListField) {
        super(numListField);
    }

    @Override
    protected Double processNumList(List<Number> numList) {
        return StatisticUtils.range(numList);
    }

}
