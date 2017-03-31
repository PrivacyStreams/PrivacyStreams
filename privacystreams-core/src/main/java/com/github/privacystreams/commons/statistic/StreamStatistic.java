package com.github.privacystreams.commons.statistic;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;

import java.util.List;

/**
 * Calculate a value (usually for statistics) based on the stream items.
 */
abstract class StreamStatistic<Tout> extends Function<List<Item>, Tout> {
    @Override
    public final Tout apply(UQI uqi, List<Item> items) {
        return this.calculate(items);
    }

    protected abstract Tout calculate(List<Item> items);
}
