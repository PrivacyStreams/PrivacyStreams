package com.github.privacystreams.commons.statistic;

import java.util.List;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.Assertions;

/**
 * Created by yuanchun on 23/12/2016.
 * A function that calculate the sum of a field in the stream.
 * The field must be a Number.
 * If there is no valid field value in the stream, the "sum" result will be 0.0
 */
final class FieldSumStatistic extends StreamStatistic<Double> {
    private final String field;

    FieldSumStatistic(String field) {
        this.field = Assertions.notNull("field", field);
        this.addParameters(field);
    }

    @Override
    public Double calculate(List<Item> items) {
        double fieldSum = 0;
        for (Item item : items) {
            Number fieldValue = item.getValueByField(this.field);

            // Skip invalid field values
            if (fieldValue == null) continue;

            fieldSum += fieldValue.doubleValue();
        }

        return fieldSum;
    }

}
