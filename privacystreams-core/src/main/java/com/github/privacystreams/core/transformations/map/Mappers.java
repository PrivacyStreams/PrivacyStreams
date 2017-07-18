package com.github.privacystreams.core.transformations.map;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.PStreamTransformation;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

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
}
