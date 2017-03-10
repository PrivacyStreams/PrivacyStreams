package com.github.privacystreams.core.transformations.map;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.SingleItemStream;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access reorder functions
 */
@PSOperatorWrapper
public class Mappers {
    /**
     * A function that transforms a multi-item stream by mapping each items with a item-to-item mapper.
     *
     * @param perItemMapper the mapper function to map each item in the stream.
     * @return the stream mapper function.
     */
    public static Function<MultiItemStream, MultiItemStream> mapEachItem(Function<Item, Item> perItemMapper) {
        return new PerItemMapper(perItemMapper);
    }

    /**
     * A function that transforms a single-item stream by mapping the item with a item-to-item mapper.
     * @param itemMapper the mapper function to map the item in the stream.
     * @return the stream mapper function.
     */
    public static Function<SingleItemStream, SingleItemStream> mapItem(Function<Item, Item> itemMapper) {
        return new ItemMapper(itemMapper);
    }
}
