package com.github.privacystreams.utils;

import android.Manifest;
import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.RequiresPermission;

import com.github.privacystreams.core.UQI;

/**
 * A helper class to access connection-related utilities.
 */

public class ConnectionUtils {

    /**
     * Check whether Wifi is connected
     * @param uqi a UQI instance
     * @return true if Wifi is connected
     */
    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public static boolean isWifiConnected(UQI uqi) {
        WifiManager wifiManager = (WifiManager) uqi.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null || !wifiManager.isWifiEnabled()) return false;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null || wifiInfo.getNetworkId() == -1) return false;
        return wifiInfo.getSupplicantState() == SupplicantState.ASSOCIATED;
    }
}
