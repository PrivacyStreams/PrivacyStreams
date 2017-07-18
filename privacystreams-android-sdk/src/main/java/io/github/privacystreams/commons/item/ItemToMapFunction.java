package io.github.privacystreams.commons.item;

import io.github.privacystreams.commons.ItemOperator;
import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;

/**
 * Get the item map of an item.
 */

class ItemIdleOperator extends ItemOperator<Item> {

    @Override
    public Item apply(UQI uqi, Item input) {
        return input;
    }
}
