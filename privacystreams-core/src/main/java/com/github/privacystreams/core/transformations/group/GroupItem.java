package com.github.privacystreams.core.transformations.group;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.List;

/**
 * An item in a stream after grouping operation.
 * GroupItem cannot be produced directly, instead, it can be generated using `groupBy` or `localGroupBy` operators.
 * An GroupItem contains two initial fields:
 * 1. The field name is the same as the field used to group (e.g. the 1st parameter of `groupBy`), and the value is the field value;
 * 2. The field name is `grouped_items`, and the value is a list of the grouped items.
 * More fields can be produced with `setGroupField` operators.
 */
@PSItem
public final class GroupItem extends Item {

    /**
     * A list of the grouped items.
     */
    @PSItemField(type = List.class)
    public static final String GROUPED_ITEMS = "grouped_items";

    GroupItem(String groupField, Object groupFieldValue, List<Item> groupedItems) {
        this.setFieldValue(groupField, groupFieldValue);
        this.setFieldValue(GROUPED_ITEMS, groupedItems);
        // exclude the sub_stream field in output.
        // this.excludeFields(GROUPED_ITEMS);
    }

}
