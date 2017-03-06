package com.github.privacystreams.device;


import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by yuanchun on 19/01/2017.
 * DeviceState item
 */

public class DeviceState extends Item {
    // type: Long
    public static final String TIMESTAMP = "timestamp";

    // type: Boolean
    private static final String STATE = "value";

    static final int SCREEN_LOCK = 0;
    static final int SCREEN_UNLOCK = 1;
    static final int SCREEN_ON = 2;
    static final int POWER_ON = 3;
    static final int POWER_OFF = 4;
    static final int BATTERY_LOW = 5;
    static final int AC_CONNECTED = 6;
    static final int AC_DISCONNECTED = 7;

    public DeviceState(long timestamp, int state) {
        this.setFieldValue(TIMESTAMP, timestamp);
        this.setFieldValue(STATE, state);
    }


    public static MultiItemStreamProvider asUpdates() {
        return new DeviceStateUpdatesProvider();
    }
}

