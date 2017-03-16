package com.github.privacystreams.commons.items;

import com.github.privacystreams.core.Item;

import java.util.List;


/**
 * select an item from the stream that has the most elements in a list field.
 * return null if fails to find an item
 */

class MostFieldSelector extends ByFieldItemSelector {
    MostFieldSelector(String listField) {
        super(listField);
    }

    @Override
    protected Item selectFrom(List<Item> items) {
        List selectedFieldValue = null;
        Item selectedItem = null;

        for (Item item : items) {
            List curFieldValue = item.getValueByField(this.field);
            if (curFieldValue == null) continue;

            if (selectedItem == null) {
                selectedItem = item;
                selectedFieldValue = curFieldValue;
            }
            else if (curFieldValue.size() > selectedFieldValue.size()) {
                selectedItem = item;
                selectedFieldValue = curFieldValue;
            }
        }

        return selectedItem;
    }
}
