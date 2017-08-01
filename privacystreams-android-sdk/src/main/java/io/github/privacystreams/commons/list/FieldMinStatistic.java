package io.github.privacystreams.commons.list;

import io.github.privacystreams.utils.StatisticUtils;

import java.util.List;

/**
 * Get the min value of a field in the stream.
 */
final class FieldMinStatistic extends NumListProcessor<Number> {

    FieldMinStatistic(String numListField) {
        super(numListField);
    }

    @Override
    protected Number processNumList(List<Number> numList) {
        return StatisticUtils.min(numList);
    }
}
