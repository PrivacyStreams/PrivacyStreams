package com.github.privacystreams.device;


import android.net.wifi.ScanResult;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A WifiAp item represents the information of a WIFI AP.
 */
@PSItem
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class WifiAp extends Item {
    @PSItemField(name = "timestamp", type = Long.class,
            description = "The timestamp of when the WIFI AP information is found.")
    private static final String TIMESTAMP = "timestamp";

    @PSItemField(name = "bssid", type = String.class,
            description = "The BSSID.")
    private static final String BSSID = "bssid";

    @PSItemField(name = "ssid", type = String.class,
            description = "The SSID.")
    private static final String SSID = "ssid";

    @PSItemField(name = "frequency", type = String.class,
            description = "The frequency.")
    private static final String FREQUENCY = "frequency";

    @PSItemField(name = "rssi", type = String.class,
            description = "The RSSI.")
    private static final String RSSI = "rssi";

    @PSItemField(name = "connected", type = Boolean.class,
            description = "Whether this AP is connected.")
    private static final String CONNECTED = "connected";

    WifiAp(ScanResult scanResult, boolean connected) {
        this.setFieldValue(TIMESTAMP, scanResult.timestamp);
        this.setFieldValue(BSSID, scanResult.BSSID);
        this.setFieldValue(SSID, scanResult.SSID);
        this.setFieldValue(FREQUENCY, scanResult.frequency);
        this.setFieldValue(RSSI, scanResult.level);
        this.setFieldValue(CONNECTED, connected);
    }

    public static MultiItemStreamProvider asScanList() {
        return new WifiApListProvider();
    }

}
