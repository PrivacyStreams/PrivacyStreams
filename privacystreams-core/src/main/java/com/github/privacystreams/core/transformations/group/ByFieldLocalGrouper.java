package com.github.privacystreams.core.transformations.group;

import java.util.ArrayList;
import java.util.List;

import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.utils.Assertions;
import com.github.privacystreams.core.utils.HashUtils;

/**
 * Created by yuanchun on 22/12/2016.
 * Group the contiguous items with same field value
 * This grouper does not change the order of items
 */

final class ByFieldLocalGrouper extends StreamGrouper {
    private final String groupField;

    ByFieldLocalGrouper(String groupField) {
        this.groupField = Assertions.notNull("groupField", groupField);
        this.addParameters(groupField);
    }

    @Override
    protected void applyInBackground(MultiItemStream input, MultiItemStream output) {
        Object lastGroupFieldValue = null;
        int lastGroupKey = 0;
        List<Item> groupItems = null;

        while (!this.isCancelled() && !output.isClosed()) {
            Item item = input.read();
            if (item == null) {
                if (groupItems != null) {
                    output.write(new GroupItem(this.groupField, lastGroupFieldValue, groupItems));
                }
                break;
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
                output.write(new GroupItem(this.groupField, lastGroupFieldValue, groupItems));
                groupItems = new ArrayList<>();
            }

            groupItems.add(item);
            lastGroupFieldValue = groupFieldValue;
            lastGroupKey = groupKey;
        }
    }

}
