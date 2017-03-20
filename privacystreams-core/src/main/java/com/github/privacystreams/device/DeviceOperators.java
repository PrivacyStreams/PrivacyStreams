package com.github.privacystreams.device;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A wrapper class of device-related functions.
 */
@PSOperatorWrapper
public class DeviceOperators {
    /**
     * Get device id.
     *
     * @return the function.
     */
    public static Function<Void, String> deviceIdGetter() {
        return new DeviceIdGetter();
    }

    /**
     * Check if wifi is connected.
     *
     * @return the function.
     */
    public static Function<Void, Boolean> wifiStatusChecker() {
        return new WifiStatusChecker();
    }
}
