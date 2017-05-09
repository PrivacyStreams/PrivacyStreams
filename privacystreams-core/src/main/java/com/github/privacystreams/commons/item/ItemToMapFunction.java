package com.github.privacystreams.commons.item;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;

import java.util.Map;

/**
 * Get the item map of an item.
 */

class ItemIdFunction extends ItemFunction<Item> {

    @Override
    public Item apply(UQI uqi, Item input) {
        return input;
    }
}
