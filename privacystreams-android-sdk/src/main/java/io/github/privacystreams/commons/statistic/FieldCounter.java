package io.github.privacystreams.commons.statistic;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.utils.Assertions;

import java.util.List;

/**
 * Count the number of valid fields in a stream.
 */
final class FieldCounter extends StreamStatistic<Integer> {

    private final String field;

    FieldCounter(String field) {
        this.field = Assertions.notNull("field", field);
        this.addParameters(field);
    }

    @Override
    protected Integer calculate(List<Item> items) {
        int count = 0;
        for (Item item : items) {
            Object fieldValue = item.getValueByField(field);
            if (fieldValue != null) count++;
        }
        return count;
    }
}
