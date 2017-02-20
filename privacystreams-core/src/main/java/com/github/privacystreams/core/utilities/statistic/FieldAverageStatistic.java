package com.github.privacystreams.core.utilities.statistic;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 23/12/2016.
 * A function that calculate the average of a field in the stream.
 * If calculation fails (e.g. there is no item in the stream), the "average" result will be null
 */
final class FieldAverageStatistic extends StreamStatistic<Double> {
    private final String field;

    FieldAverageStatistic(String field) {
        this.field = field;
    }

    @Override
    public Double calculate(List<Item> items) {
        double fieldValueSum = 0.0;
        int fieldCount = 0;
        for (Item item : items) {
            Number fieldValue = item.getValueByField(this.field);

            // Skip invalid field values
            if (fieldValue == null) continue;

            fieldValueSum += fieldValue.doubleValue();
            fieldCount += 1;
        }

        // If there is no valid field value, return null.
        if (fieldCount == 0) return null;
        return fieldValueSum / fieldCount;
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(field);
        return parameters;
    }
}
