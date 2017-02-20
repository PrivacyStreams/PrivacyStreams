package com.github.privacystreams.core.transformations.group;

import java.util.List;

import com.github.privacystreams.core.Item;

/**
 * Created by yuanchun on 22/12/2016.
 * A GroupItem represent the item in a grouped stream
 */
public final class GroupItem extends Item {
    public static final String GROUPED_ITEMS = "grouped_items";

    GroupItem(String groupField, Object groupFieldValue, List<Item> groupedItems) {
        this.setFieldValue(groupField, groupFieldValue);
        this.setFieldValue(GROUPED_ITEMS, groupedItems);
        // exclude the sub_stream field in output.
        // this.excludeFields(GROUPED_ITEMS);
    }

}
