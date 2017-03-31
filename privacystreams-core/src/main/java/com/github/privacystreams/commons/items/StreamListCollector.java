package com.github.privacystreams.commons.items;

import com.github.privacystreams.commons.ItemsFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;

import java.util.List;

/**
 * Collect the stream to a list.
 * Each item in the list is an instance of Item.
 */

class StreamListCollector extends ItemsFunction<List<Item>> {
    @Override
    public List<Item> apply(UQI uqi, List<Item> items) {
        return items;
    }

}
