package com.github.privacystreams.device;


import android.net.wifi.ScanResult;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by yuanchun on 07/12/2016.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class WifiAp extends Item {
    private static final String TIMESTAMP = "timestamp";
    private static final String BSSID = "bssid";
    private static final String SSID = "ssid";
    private static final String FREQUENCY = "frequency";
    private static final String RSSI = "rssi";
    private static final String CONNECTED = "connected";

    WifiAp(ScanResult scanResult, boolean connected) {
        this.setFieldValue(TIMESTAMP, scanResult.timestamp);

        this.setFieldValue(BSSID, scanResult.BSSID);
        this.setFieldValue(SSID, scanResult.SSID);
        this.setFieldValue(FREQUENCY, scanResult.frequency);
        this.setFieldValue(RSSI, scanResult.level);
        this.setFieldValue(CONNECTED, connected);
    }

    public static MultiItemStreamProvider asUpdates(int samplingPeriodInSeconds) {
        return new WifiUpdatesProvider(samplingPeriodInSeconds);
    }

}
