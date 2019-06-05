package io.github.privacystreams.multi;

import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

class MultiItemGetLogIndexField extends MultiProcessor<Object>{
    private int itemIndex;
    private String itemField;
    private int logIndex;

    MultiItemGetLogIndexField(int itemIndex, int logIndex, String itemField){
        this.itemIndex = Assertions.notNull("itemIndex", itemIndex);
        this.addParameters(itemIndex);
        this.itemField = Assertions.notNull("itemField", itemField);
        this.addParameters(itemField);
        this.logIndex = Assertions.notNull("logIndex", logIndex);
        this.addParameters(logIndex);
    }

    @Override
    protected Object processMulti(UQI uqi, Item item) {
        List<Object> items = item.getValueByField("items");
        Assertions.notNull("", items);
        List<Item> ilist = (List<Item>) (items.get(itemIndex));

        Assertions.notNull("ilist", ilist);

        if (logIndex < 0 || logIndex >= ilist.size()) {
            return ilist.get(ilist.size() - 1).getValueByField(itemField);
        } else {
            return ilist.get(logIndex).getValueByField(itemField);
        }
    }
}



