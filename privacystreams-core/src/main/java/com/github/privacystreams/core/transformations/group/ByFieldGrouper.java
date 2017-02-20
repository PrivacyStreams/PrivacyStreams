package com.github.privacystreams.core.transformations.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.utils.HashUtils;

/**
 * Created by yuanchun on 22/12/2016.
 * Group the items with same field value together
 * This grouper will change the order of items
 */
final class ByFieldGrouper extends StreamGrouper {
    private final String groupField;

    ByFieldGrouper(String groupField) {
        this.groupField = groupField;
    }

    @Override
    protected void applyInBackground(MultiItemStream input, MultiItemStream output) {
        if (this.isCancelled() || output.isClosed()) return;

        HashMap<Integer, Object> groupFieldMap = new HashMap<>();
        HashMap<Integer, List<Item>> groupItemsMap = new HashMap<>();

        List<Item> items = input.readAll();
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
            output.write(new GroupItem(this.groupField, groupFieldMap.get(groupKey), groupItemsMap.get(groupKey)));
        }
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add(groupField);
        return parameters;
    }
}
