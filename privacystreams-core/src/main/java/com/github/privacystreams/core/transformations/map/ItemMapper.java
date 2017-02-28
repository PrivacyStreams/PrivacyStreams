package com.github.privacystreams.core.transformations.map;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.S2STransformation;

import static com.github.privacystreams.core.utils.Assertions.notNull;

/**
 * Created by yuanchun on 27/12/2016.
 * Map an item by changing the fields in an item.
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
