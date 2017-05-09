package com.github.privacystreams.commons.statistic;

import com.github.privacystreams.utils.StatisticUtils;

import java.util.List;

/**
 * Calculate the RMS (root mean square) of a field in the stream.
 * If calculation fails (e.g. there is no item in the stream), the result will be null
 */
final class FieldRMSStatistic extends FieldStatistic<Double> {

    FieldRMSStatistic(String numField) {
        super(numField);
    }

    @Override
    protected Double processNumList(List<Number> numList) {
        return StatisticUtils.rms(numList);
    }
}
