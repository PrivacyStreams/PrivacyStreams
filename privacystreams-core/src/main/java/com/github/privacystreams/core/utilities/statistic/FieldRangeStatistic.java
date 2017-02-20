package com.github.privacystreams.core.utilities.statistic;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 23/12/2016.
 * A function that calculate the range of a field in the stream.
 * The field must be a Number.
 * If calculation fails (e.g. there is no item in the stream), the "range" result will be null
 */
final class FieldRangeStatistic extends StreamStatistic<Double> {
    private final String field;

    FieldRangeStatistic(String field) {
        this.field = field;
    }

    @Override
    public Double calculate(List<Item> items) {
        Double fieldValueMin = null, fieldValueMax = null;
        for (Item item : items) {
            Number fieldValue = item.getValueByField(this.field);

            // Skip invalid field values
            if (fieldValue == null) continue;

            double fieldDoubleValue = fieldValue.doubleValue();

            // If this is the first valid field value, initialize fieldValueMax and fieldValueMin
            if (fieldValueMax == null) fieldValueMax = fieldDoubleValue;
            if (fieldValueMin == null) fieldValueMin = fieldDoubleValue;

            // Change the field max/min value
            if (fieldDoubleValue > fieldValueMax) fieldValueMax = fieldDoubleValue;
            if (fieldDoubleValue < fieldValueMin) fieldValueMin = fieldDoubleValue;
        }

        // If there is no valid field value, return null.
        if (fieldValueMin == null) return null;
        return fieldValueMax - fieldValueMin;
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(field);
        return parameters;
    }
}
