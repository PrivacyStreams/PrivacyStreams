package io.github.privacystreams.device;


import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * A WifiAp item represents the information of a WIFI AP.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
@PSItem
public class WifiAp extends Item {

    /**
     * The timestamp of when the WIFI AP information is found.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * The BSSID.
     */
    @PSItemField(type = String.class)
    public static final String BSSID = "bssid";

    /**
     * The SSID.
     */
    @PSItemField(type = String.class)
    public static final String SSID = "ssid";

    /**
     * The frequency.
     */
    @PSItemField(type = String.class)
    public static final String FREQUENCY = "frequency";

    /**
     * The RSSI.
     */
    @PSItemField(type = String.class)
    public static final String RSSI = "rssi";

    /**
     * Whether this AP is connected.
     */
    @PSItemField(type = Boolean.class)
    public static final String STATUS = "status";


    public static final String STATUS_CONNECTED = "connected";
    public static final String STATUS_DISCONNECTED = "disconnected";
    public static final String STATUS_SCANNED = "scanned";

    WifiAp(ScanResult scanResult, String status) {
        this.setFieldValue(TIMESTAMP, scanResult.timestamp);
        this.setFieldValue(BSSID, scanResult.BSSID);
        this.setFieldValue(SSID, scanResult.SSID);
        this.setFieldValue(FREQUENCY, scanResult.frequency);
        this.setFieldValue(RSSI, scanResult.level);
        this.setFieldValue(STATUS, status);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    WifiAp(WifiInfo wifiInfo, String status) {
        this.setFieldValue(TIMESTAMP, System.currentTimeMillis());
        this.setFieldValue(BSSID, wifiInfo.getBSSID());
        this.setFieldValue(SSID, wifiInfo.getSSID());
        this.setFieldValue(FREQUENCY, wifiInfo.getFrequency());
        this.setFieldValue(RSSI, wifiInfo.getRssi());
        this.setFieldValue(STATUS, status);
    }

    WifiAp(WifiAp another) {
        for (String key : another.toMap().keySet()) {
            this.setFieldValue(key, another.getValueByField(key));
        }
    }

    /**
     * Provide a list of WifiAp items from WIFI scan result.
     * This provider requires `android.permission.ACCESS_COARSE_LOCATION`,
     * `android.permission.CHANGE_WIFI_STATE`, and `android.permission.ACCESS_WIFI_STATE` permission.
     *
     * @return the provider function.
     */

    // @RequiresPermission(allOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_WIFI_STATE})
    public static PStreamProvider getScanResults() {
        return new WifiApListProvider();
    }

    public static PStreamProvider getUpdateStatus() {
        return new WifiUpdatesProvider();
    }

}
