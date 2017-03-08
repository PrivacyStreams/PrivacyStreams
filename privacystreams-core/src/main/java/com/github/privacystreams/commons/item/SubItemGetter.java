package com.github.privacystreams.commons.item;

import java.util.Map;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.commons.ItemFunction;

import static com.github.privacystreams.utils.Assertions.notNull;

/**
 * Created by yuanchun on 27/12/2016.
 * A function that gets the value of a field
 */

final class SubItemGetter extends ItemFunction<Item> {
    private final String subItemField;

    SubItemGetter(String subItemField) {
        this.subItemField = notNull("subItemField", subItemField);
        this.addParameters(subItemField);
    }

    @Override
    public Item apply(UQI uqi, Item input) {
        Map<String, Object> subItemMap = input.getValueByField(this.subItemField);
        return new Item(subItemMap);
    }

}
