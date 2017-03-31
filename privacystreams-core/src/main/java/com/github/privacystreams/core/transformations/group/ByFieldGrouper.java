package com.github.privacystreams.core.transformations.group;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.Assertions;
import com.github.privacystreams.utils.HashUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Group the items with same field value together.
 * This grouper will change the order of items.
 */
final class ByFieldGrouper extends StreamGrouper {
    private final String groupField;

    ByFieldGrouper(String groupField) {
        this.groupField = Assertions.notNull("groupField", groupField);
        this.addParameters(groupField);
    }

    private void group(List<Item> items) {
        HashMap<Integer, Object> groupFieldMap = new HashMap<>();
        HashMap<Integer, List<Item>> groupItemsMap = new HashMap<>();

        for (Item item : items) {
            Object groupFieldValue = item.getValueByField(this.groupField);
            int groupKey = HashUtils.valueHash(groupFieldValue);

            if (!groupItemsMap.containsKey(groupKey)) {
                groupFieldMap.put(groupKey, groupFieldValue);
                groupItemsMap.put(groupKey, new ArrayList<Item>());
            }
            groupItemsMap.get(groupKey).add(item);
        }

        for (Integer groupKey : groupItemsMap.keySet()) {
            this.output(new GroupItem(this.groupField, groupFieldMap.get(groupKey), groupItemsMap.get(groupKey)));
        }
        this.finish();
    }

    private transient List<Item> items;
    @Override
    protected void onInput(Item item) {
        if (this.items == null) this.items = new ArrayList<>();
        if (item.isEndOfStream()) {
            this.group(items);
            return;
        }
        this.items.add(item);
    }
}
