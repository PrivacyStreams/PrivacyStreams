package com.github.privacystreams.core.transformations.map;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2MTransformation;

import static com.github.privacystreams.utils.Assertions.notNull;

/**
 * Created by yuanchun on 28/11/2016.
 * A function that maps a stream to a stream.
 */
final class PerItemMapper extends M2MTransformation {

    private final Function<Item, Item> itemMapper;

    PerItemMapper(final Function<Item, Item> itemMapper) {
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
