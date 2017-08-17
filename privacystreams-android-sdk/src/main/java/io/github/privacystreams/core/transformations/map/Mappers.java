package io.github.privacystreams.core.transformations.map;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamTransformation;
import io.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access reorder functions
 */
@PSOperatorWrapper
public class Mappers {
    /**
     * Transform a multi-item stream by mapping each items with a item-to-item mapper.
     *
     * @param perItemMapper the mapper function to map each item in the stream.
     * @return the stream mapper function.
     */
    public static PStreamTransformation mapEachItem(Function<Item, Item> perItemMapper) {
        return new PerItemMapper(perItemMapper);
    }

    /**
     * Make the items be sent in a fixed interval.
     *
     * @param fixedInterval the fixed interval in milliseconds.
     * @return the stream mapper function.
     */
    public static PStreamTransformation inFixedInterval(long fixedInterval) {
        return new FixedIntervalMapper(fixedInterval);
    }
}
