package com.github.privacystreams.device;


import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A DeviceEvent item represents an event about the device.
 */
@PSItem
public class DeviceEvent extends Item {

    /**
     * The timestamp of when the event is happened.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * The type of the event, could be "screen", "boot", "battery", or "ringer".
     */
    @PSItemField(type = String.class)
    public static final String TYPE = "type";

    public static final String TYPE_SCREEN = "screen";
    public static final String TYPE_BOOT = "boot";
    public static final String TYPE_BATTERY = "battery";
    public static final String TYPE_RINGER = "ringer";

    /**
     * The event name. For screen events, could be "on", "off", or "user_present";
     * For boot events, could be "boot_completed", or "shutdown";
     * For battery events, could be "low", "okay", "ac_connected", or "ac_disconnected";
     * For ringer events, could be "silent", "vibrate", or "normal".
     */
    @PSItemField(type = String.class)
    public static final String EVENT = "event";

    public static final String EVENT_SCREEN_OFF = "off";
    public static final String EVENT_SCREEN_USER_PRESENT = "user_present";
    public static final String EVENT_SCREEN_ON = "on";

    public static final String EVENT_BOOT_COMPLETED = "boot_completed";
    public static final String EVENT_BOOT_SHUTDOWN = "shutdown";

    public static final String EVENT_BATTERY_LOW = "low";
    public static final String EVENT_BATTERY_OKAY = "okay";
    public static final String EVENT_BATTERY_AC_CONNECTED = "ac_connected";
    public static final String EVENT_BATTERY_AC_DISCONNECTED = "ac_disconnected";

    public static final String EVENT_RINGER_SILENT = "silent";
    public static final String EVENT_RINGER_VIBRATE = "vibrate";
    public static final String EVENT_RINGER_NORMAL = "normal";

    DeviceEvent(long timestamp, String type, String event) {
        this.setFieldValue(TIMESTAMP, timestamp);
        this.setFieldValue(TYPE, type);
        this.setFieldValue(EVENT, event);
    }

    /**
     * Provide a live stream of device events, including screen/boot/battery/ringer events.
     *
     * @return the provider function.
     */
    public static MStreamProvider asUpdates() {
        return new DeviceEventUpdatesProvider();
    }
}

