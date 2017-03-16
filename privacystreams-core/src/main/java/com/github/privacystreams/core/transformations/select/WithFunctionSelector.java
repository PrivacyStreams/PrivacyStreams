package com.github.privacystreams.core.transformations.select;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2STransformation;
import com.github.privacystreams.utils.Assertions;

import java.util.ArrayList;
import java.util.List;


/**
 * select an item from the stream
 * return null if fails to find an item
 */

class WithFunctionSelector extends M2STransformation {
    protected final Function<List<Item>, Item> selector;

    WithFunctionSelector(Function<List<Item>, Item> selector) {
        this.selector = Assertions.notNull("selector", selector);
        this.addParameters(selector);
    }

    private transient List<Item> items;
    @Override
    protected void onInput(Item item) {
        if (this.items == null) this.items = new ArrayList<>();
        if (item.isEndOfStream()) {
            Item selectedItem = this.selector.apply(this.getUQI(), items);
            this.output(selectedItem);
            return;
        }
        this.items.add(item);
    }
}
