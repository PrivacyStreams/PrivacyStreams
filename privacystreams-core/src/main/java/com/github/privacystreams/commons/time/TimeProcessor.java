package com.github.privacystreams.commons.time;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

/**
 * Process the timestamp specified by a field in an item.
 */
abstract class TimeProcessor<Tout> extends ItemFunction<Tout> {

    private final String timestampField;

    TimeProcessor(String timestampField) {
        this.timestampField = Assertions.notNull("timestampField", timestampField);
        this.addParameters(timestampField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        long timestamp = input.getValueByField(this.timestampField);
        return this.processTimestamp(timestamp);
    }

    protected abstract Tout processTimestamp(long timestamp);
}
