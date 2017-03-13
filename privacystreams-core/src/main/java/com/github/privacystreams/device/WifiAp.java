package com.github.privacystreams.device;


import android.net.wifi.ScanResult;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MStream;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

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
    public static final String CONNECTED = "connected";

    WifiAp(ScanResult scanResult, boolean connected) {
        this.setFieldValue(TIMESTAMP, scanResult.timestamp);
        this.setFieldValue(BSSID, scanResult.BSSID);
        this.setFieldValue(SSID, scanResult.SSID);
        this.setFieldValue(FREQUENCY, scanResult.frequency);
        this.setFieldValue(RSSI, scanResult.level);
        this.setFieldValue(CONNECTED, connected);
    }

    /**
     * Provide a list of WifiAp items from WIFI scan result.
     * @return the provider function.
     */
    public static Function<Void, MStream> asScanList() {
        return new WifiApListProvider();
    }

}
