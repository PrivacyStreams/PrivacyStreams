package io.github.privacystreams.multi;

import java.util.ArrayList;
import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

class MultiItemGetLogField extends MultiProcessor<Object> {
    private int itemIndex;
    private String itemField;

    MultiItemGetLogField (int itemIndex, String itemField){
        this.itemIndex = Assertions.notNull("itemIndex", itemIndex);
        this.addParameters(itemIndex);
        this.itemField = Assertions.notNull("itemField", itemField);
        this.addParameters(itemField);
    }

    @Override
    protected List<Object> processMulti(UQI uqi, Item item) {
        List<Object> items = item.getValueByField("items");
        Assertions.notNull("", items);
        List<Item> ilist = (List<Item>)(items.get(itemIndex));

        List<Object> fieldList = new ArrayList<>();
        Assertions.notNull("ilist", ilist);
        for (int i = 0; i < ilist.size(); i++) {
            fieldList.add(ilist.get(i).getValueByField(itemField));
        }

        return fieldList;
    }
}
