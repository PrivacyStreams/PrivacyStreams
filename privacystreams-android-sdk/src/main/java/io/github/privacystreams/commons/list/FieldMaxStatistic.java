package io.github.privacystreams.commons.list;

import io.github.privacystreams.utils.StatisticUtils;

import java.util.List;

/**
 * Get the max value of a field in the stream.
 */
final class FieldMaxStatistic extends NumListProcessor<Number> {

    FieldMaxStatistic(String numListField) {
        super(numListField);
    }

    @Override
    protected Number processNumList(List<Number> numList) {
        return StatisticUtils.max(numList);
    }
}
