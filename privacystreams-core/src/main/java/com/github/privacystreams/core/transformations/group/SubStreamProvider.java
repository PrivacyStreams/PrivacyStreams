package com.github.privacystreams.core.transformations.group;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by yuanchun on 22/12/2016.
 * A provider that produces the sub stream
 */

final class SubStreamProvider extends MultiItemStreamProvider {
    private List<Item> items;

    SubStreamProvider(List<Item> items) {
        this.items = items;
        this.addParameters(items);
    }

    @Override
    protected final void provide(MultiItemStream output) {
        for (Item item : this.items) {
            output.write(item);
        }
        output.write(null);
    }
}
