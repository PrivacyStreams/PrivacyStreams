package io.github.privacystreams.multi;

import java.util.List;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

@PSItem
public class MultiItem extends Item{
    public enum ItemType  {ACCELERATION, AIR_PRESSURE, AMBIENT_TEMPERATURE, AUDIO_FROMSTORAGE,
        AUDIO, BLUETOOTH_DEVICE, CALENDAR, CALL, CONTACT, DEVICE_EVENT, DEVICE_STATE, EMAIL,
        EMPTY_ITEM, GEOLOCATION_CURRENT, GEOLOCATION_LASTKNOWN, GRAVITY, GYROSCOPE, IMAGE_BG,
        IMAGE_FROMSTORAGE, IMAGE_TAKE, LIGHT, LINEAR_ACCELERATION, MESSAGE, RELATIVE_HUMIDITY,
        ROTATION_VECTOR, STEP_COUNTER, TEST_ITEM, WIFI_AP }

    @PSItemField(type = List.class)
    public static final String ITEM_TYPES = "item_types";

    @PSItemField(type = List.class)
    public static final String ITEMS = "items";


    MultiItem(List<ItemType> item_types, List<Object> items) {
        this.setFieldValue(ITEM_TYPES, item_types);
        this.setFieldValue(ITEMS, items);
    }

    public static PStreamProvider oneshot(List<ItemType> item_types, List<Purpose> purposes) {
        return new MultiItemOnce(item_types, purposes);
    }

    public static PStreamProvider oneshot(List<ItemType> item_types, List<Purpose> purposes, long duration, int limit) {
        return new MultiItemOnce(item_types, purposes, duration, limit);
    }
    public static PStreamProvider oneshot(List<ItemType> item_types, List<Purpose> purposes, long duration, int limit,
                                          int cameraId, int mask, String level, long afterTime, long beforeTime) {
        return new MultiItemOnce(item_types, purposes, duration, limit, cameraId, mask, level, afterTime, beforeTime);
    }
    public static PStreamProvider periodic(List<ItemType> item_types, List<Purpose> purposes, long interval, long duration, int limit) {
        return new MultiItemPeriodic(item_types, purposes, interval, duration, limit);
    }

    public static PStreamProvider periodic(List<ItemType> item_types, List<Purpose> purposes, long interval, long duration, int limit,
                                           int cameraId, int mask, String level, long afterTime, long beforeTime) {
        return new MultiItemPeriodic(item_types, purposes, interval, duration, limit, cameraId, mask, level, afterTime, beforeTime);
    }
}
