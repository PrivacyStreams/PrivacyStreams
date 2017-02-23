package com.github.privacystreams.core.transformations.map;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2MTransformation;

import static com.github.privacystreams.core.utils.Assertions.notNull;

/**
 * Created by yuanchun on 28/11/2016.
 * A function that maps a stream to a stream.
 */
final class PerItemMapper extends M2MTransformation {

    @Override
    protected final void applyInBackground(MultiItemStream input, MultiItemStream output) {
        while (!this.isCancelled() && !output.isClosed()) {
            Item item = input.read();
            if (item == null) break;
            Item outputItem = this.itemMapper.apply(this.getUQI(), item);
            output.write(outputItem);
        }
    }

    private final Function<Item, Item> itemMapper;

    PerItemMapper(final Function<Item, Item> itemMapper) {
        this.itemMapper = notNull("itemMapper", itemMapper);
        this.addParameters(itemMapper);
    }
}
