package io.github.privacystreams.device;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

import java.util.List;

/**
 * A DeviceEvent item represents a snapshot of device state.
 */
@PSItem
public class DeviceState extends Item {
    /**
     * The list of currently scanned bluetooth device.
     */
    @PSItemField(type = List.class)
    public static final String BT_DEVICE_LIST = "bt_device_list";

    /**
     * The list of currently scanned Wifi APs.
     */
    @PSItemField(type = List.class)
    public static final String WIFI_AP_LIST = "wifi_ap_list";

    /**
     * The current battery level.
     */
    @PSItemField(type = Float.class)
    public static final String BATTERY_LEVEL = "battery_level";

    /**
     * Whether the current device is connected to network
     */
    @PSItemField(type = Boolean.class)
    public static final String IS_CONNECTED = "is_connected";

    /**
     * The connected WiFi AP BSSID, could be null if the device is not connected to WiFi
     */
    @PSItemField(type = String.class)
    public static final String WIFI_BSSID = "wifi_bssid";

    /**
     * Whether the screen is on
     */
    @PSItemField(type = String.class)
    public static final String IS_SCREEN_ON = "is_screen_on";

    public static class Masks {
        public static final int BLUETOOTH_DEVICE_LIST = 1;
        public static final int WIFI_AP_LIST = 1 << 1;
        public static final int BATTERY_LEVEL = 1 << 2;
        public static final int CONNECTION_INFO = 1 << 3;
        public static final int SCREEN_STATE = 1 << 4;
    }

    /**
     * Provide a live stream of device states, including bluetooth, wifi, battery level, and/or foreground apps etc.
     *
     * @param interval the interval between each two device state snapshots
     * @param mask the mask of device state type, could be `DeviceState.Masks.BT_DEVICE_LIST`, `DeviceState.Masks.WIFI_AP_LIST`, etc.
     * @return the provider function
     */
    public static PStreamProvider asUpdates(long interval, int mask) {
        return new DeviceStateUpdatesProvider(interval, mask);
    }
}
