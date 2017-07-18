package com.github.privacystreams.commons.item;

import com.github.privacystreams.commons.ItemOperator;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;

/**
 * Get the item map of an item.
 */

class ItemIdleOperator extends ItemOperator<Item> {

    @Override
    public Item apply(UQI uqi, Item input) {
        return input;
    }
}
