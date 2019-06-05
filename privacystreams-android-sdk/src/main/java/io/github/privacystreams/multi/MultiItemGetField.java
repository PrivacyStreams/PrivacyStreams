package io.github.privacystreams.multi;

import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

class MultiItemGetField extends MultiProcessor<Object> {
    private int itemIndex;
    private String itemField;
    MultiItemGetField(int itemIndex, String itemField) {
        this.itemIndex = Assertions.notNull("itemIndex", itemIndex);
        this.addParameters(itemIndex);
        this.itemField = Assertions.notNull("itemField", itemField);
        this.addParameters(itemField);
    }

    @Override
    protected Object processMulti(UQI uqi, Item item) {
        List<Object> items = item.getValueByField("items");
        Item i = (Item)items.get(itemIndex);
        Assertions.notNull("i", i);
        return i.getValueByField(itemField);
    }
}
