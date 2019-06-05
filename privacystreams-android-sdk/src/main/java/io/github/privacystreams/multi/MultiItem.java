package io.github.privacystreams.multi;

import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

@PSItem
public class MultiItem extends Item{
    public enum ItemType  {ACCELERATION, AIR_PRESSURE, AMBIENT_TEMPERATURE, AUDIO, BLUETOOTH_DEVICE, BROWSER_SEARCH,
            BROWSER_VISIT, CALENDAR_EVENT, CALL, CONTACT, DEVICE_EVENT, DEVICE_STATE, EMAIL, EMPTY_ITEM,
        GEOLOCATION, GRAVITY, GYROSCOPE, IMAGE, LIGHT, LINEAR_ACCELERATION, MESSAGE,
        NOTIFICATION, RELATIVE_HUMIDITY, ROTATION_VECTOR, STEP_COUNTER, TEST_ITEM, WIFI_AP }
    @PSItemField(type = List.class)
    public static final String ITEM_TYPES = "item_types";

    @PSItemField(type = List.class)
    public static final String ITEMS = "items";


    MultiItem(List<ItemType> item_types, List<Object> items) {
        this.setFieldValue(ITEM_TYPES, item_types);
        this.setFieldValue(ITEMS, items);
    }

    public static PStreamProvider oneshot(List<ItemType> item_types, List<Purpose> purposes, long duration, int limit) {
        return new MultiItemOnce(item_types, purposes, duration, limit);
    }

/*    public static PStreamProvider periodic(List<ItemType> item_types, long interval) {
        return new MultiItemPeriodic(item_types, interval);
    }

    public static PStreamProvider periodic(List<ItemType> item_types, long duration, long interval) {
        return new MultiItemPeriodic(item_types, duration, interval);
    }*/
}
