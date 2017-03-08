package com.github.privacystreams.device;


import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by yuanchun on 19/01/2017.
 * DeviceState item
 */

public class DeviceStateChange extends Item {
    // type: Long
    private static final String TIMESTAMP = "timestamp";
    // type: Boolean
    private static final String STATE = "value";

    // type: String
    private static final String TYPE = "type";

    static final String SCREEN_LOCK = "lock";
    static final String SCREEN_UNLOCK = "unlock";
    static final String SCREEN_ON = "screen on";

    static final String POWER_ON = "power on";
    static final String POWER_OFF = "power off";

    static final String BATTERY_LOW = "battery low";
    static final String BATTERY_OK = "battery ok";
    static final String AC_CONNECTED = "ac connected";
    static final String AC_DISCONNECTED = "ac disconnected";

    static final String RINGER_SILENT = "ringer silent";
    static final String RINGER_VIBRATE = "ringer vibrate";
    static final String RINGER_NORMAL = "ringer normal";

    DeviceStateChange(long timestamp, String type, String state) {
        this.setFieldValue(TIMESTAMP, timestamp);
        this.setFieldValue(TYPE, type);
        this.setFieldValue(STATE, state);
    }

    public static MultiItemStreamProvider asUpdates() {
        return new DeviceStateUpdatesProvider();
    }
}

