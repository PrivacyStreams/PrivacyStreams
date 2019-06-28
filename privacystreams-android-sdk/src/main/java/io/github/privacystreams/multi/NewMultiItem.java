package io.github.privacystreams.multi;

import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;

public class NewMultiItem extends Item {

    @PSItemField(type = List.class)
    public static final String ITEM_TYPES = "item_types";

    @PSItemField(type = List.class)
    public static final String ITEMS = "items";


    NewMultiItem(List<ItemType> item_types, List<ItemWrapper> items) {
        this.setFieldValue(ITEM_TYPES, item_types);
        this.setFieldValue(ITEMS, items);
    }

    public static PStreamProvider oneshot(List<ItemType> item_types) {
        return new NewMultiItemOnce(item_types);
    }

    public static PStreamProvider periodic(List<ItemType> item_types, long interval) {
        return new NewMultiItemPeriodic(item_types, interval);
    }
}
