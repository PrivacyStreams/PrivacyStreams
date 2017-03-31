package com.github.privacystreams.core.transformations.map;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.S2STransformation;

import static com.github.privacystreams.utils.Assertions.notNull;

/**
 * Map the item with a mapper function.
 */
final class ItemMapper extends S2STransformation {
    private final Function<Item, Item> itemMapper;

    ItemMapper(final Function<Item, Item> itemMapper) {
        this.itemMapper = notNull("itemMapper", itemMapper);
        this.addParameters(itemMapper);
    }

    @Override
    protected final void onInput(Item item) {
        if (item.isEndOfStream()) {
            this.finish();
            return;
        }
        Item outputItem = this.itemMapper.apply(this.getUQI(), item);
        this.output(outputItem);
    }
}
