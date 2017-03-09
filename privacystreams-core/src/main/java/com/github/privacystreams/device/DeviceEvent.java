package com.github.privacystreams.device;


import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by yuanchun on 19/01/2017.
 * DeviceState item
 */

public class DeviceEvent extends Item {
    // type: Long
    private static final String TIMESTAMP = "timestamp";
    // type: Boolean
    private static final String Event = "event";
    // type: String
    private static final String TYPE = "type";

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

    public static MultiItemStreamProvider asUpdates() {
        return new DeviceEventUpdatesProvider();
    }
}

