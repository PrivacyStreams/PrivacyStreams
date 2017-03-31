package com.github.privacystreams.core.transformations.group;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.Assertions;
import com.github.privacystreams.utils.HashUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Group the contiguous items with same field value.
 * This grouper does not change the order of items.
 */

final class ByFieldLocalGrouper extends StreamGrouper {
    private final String groupField;

    ByFieldLocalGrouper(String groupField) {
        this.groupField = Assertions.notNull("groupField", groupField);
        this.addParameters(groupField);
    }

    private transient Object lastGroupFieldValue = null;
    private transient int lastGroupKey = 0;
    private transient List<Item> groupItems = null;

    @Override
    protected void onInput(Item item) {
        if (item.isEndOfStream()) {
            if (groupItems != null) {
                this.output(new GroupItem(this.groupField, lastGroupFieldValue, groupItems));
            }
            this.finish();
            return;
        }

        Object groupFieldValue = item.getValueByField(this.groupField);
        int groupKey = HashUtils.valueHash(groupFieldValue);

        // If it is the first stream item, initialize lastGroupFieldValue and groupItems
        if (groupItems == null) {
            lastGroupFieldValue = groupFieldValue;
            lastGroupKey = groupKey;
            groupItems = new ArrayList<>();
        }

        // If group key changes, stop the last group stream and create a new group stream
        if (groupKey != lastGroupKey) {
            this.output(new GroupItem(this.groupField, lastGroupFieldValue, groupItems));
            groupItems = new ArrayList<>();
        }

        groupItems.add(item);
        lastGroupFieldValue = groupFieldValue;
        lastGroupKey = groupKey;
    }

}
