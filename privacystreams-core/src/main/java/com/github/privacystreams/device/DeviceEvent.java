package com.github.privacystreams.device;


import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MStream;
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
     * The type of the event, could be "screen", "boot", "battery", "ringer", etc.
     */
    @PSItemField(type = String.class)
    public static final String TYPE = "type";

    /**
     * The event name. For screen events, could be on/off/user_present;
     * For boot events, could be boot_completed/shutdown;
     * For battery events, could be low/okay/ac_connected/ac_disconnected;
     * For ringer events, could be silent/vibrate/normal.
     */
    @PSItemField(type = String.class)
    public static final String Event = "event";

    public static class Types {
        public static final String SCREEN = "screen";
        public static final String BOOT = "boot";
        public static final String BATTERY = "battery";
        public static final String RINGER = "ringer";
    }

    public static class ScreenEvents {
        public static final String OFF = "off";
        public static final String USER_PRESENT = "user_present";
        public static final String ON = "on";
    }

    public static class BootEvents {
        public static final String BOOT_COMPLETED = "boot_completed";
        public static final String SHUTDOWN = "shutdown";
    }

    public static class BatteryEvents {
        public static final String LOW = "low";
        public static final String OKAY = "okay";
        public static final String AC_CONNECTED = "ac_connected";
        public static final String AC_DISCONNECTED = "ac_disconnected";
    }

    public static class RingerEvents {
        public static final String SILENT = "silent";
        public static final String VIBRATE = "vibrate";
        public static final String NORMAL = "normal";
    }

    DeviceEvent(long timestamp, String type, String state) {
        this.setFieldValue(TIMESTAMP, timestamp);
        this.setFieldValue(TYPE, type);
        this.setFieldValue(Event, state);
    }

    /**
     * Provide a live stream of device events, including screen/boot/battery/ringer events.
     *
     * @return the provider function.
     */
    public static Function<Void, MStream> asUpdates() {
        return new DeviceEventUpdatesProvider();
    }
}

