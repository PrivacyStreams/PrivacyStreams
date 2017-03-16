package com.github.privacystreams.core.transformations.map;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2MTransformation;
import com.github.privacystreams.core.transformations.S2STransformation;
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
    public static M2MTransformation mapEachItem(Function<Item, Item> perItemMapper) {
        return new PerItemMapper(perItemMapper);
    }

    /**
     * Transform a single-item stream by mapping the item with a item-to-item mapper.
     * @param itemMapper the mapper function to map the item in the stream.
     * @return the stream mapper function.
     */
    public static S2STransformation mapItem(Function<Item, Item> itemMapper) {
        return new ItemMapper(itemMapper);
    }
}
