package com.github.privacystreams.core.transformations.group;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSItem;

import java.util.List;

/**
 * A GroupItem represent the item in a grouped stream.
 * GroupItem cannot be produced directly, instead it can be generated using groupBy or localGroupBy functions.
 */
@PSItem
public final class GroupItem extends Item {
    public static final String GROUPED_ITEMS = "grouped_items";

    GroupItem(String groupField, Object groupFieldValue, List<Item> groupedItems) {
        this.setFieldValue(groupField, groupFieldValue);
        this.setFieldValue(GROUPED_ITEMS, groupedItems);
        // exclude the sub_stream field in output.
        // this.excludeFields(GROUPED_ITEMS);
    }

}
