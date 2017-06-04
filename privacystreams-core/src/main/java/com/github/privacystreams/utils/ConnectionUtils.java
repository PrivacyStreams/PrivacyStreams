package com.github.privacystreams.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.github.privacystreams.core.UQI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


/**
 * A helper class to access connection-related utilities.
 */

public class ConnectionUtils {
    /**
     * Check whether Wifi is connected
     * @param uqi a UQI instance
     * @return true if Wifi is connected
     */
    // @RequiresPermission(value = Manifest.permission.ACCESS_WIFI_STATE)
    public static boolean isWifiConnected(UQI uqi) {
        WifiManager wifiManager = (WifiManager) uqi.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null || !wifiManager.isWifiEnabled()) return false;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null || wifiInfo.getNetworkId() == -1) return false;
        return wifiInfo.getSupplicantState() == SupplicantState.ASSOCIATED;
    }

    /* Checks whether the device currently has a network connection.
      * @return true if the device has a network connection, false otherwise.
      */
    public static boolean isDeviceOnline(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    public static boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();

        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(context);
        Log.e("Test","Avalability "+connectionStatusCode);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

         /* Attempt to resolve a missing, out-of-date, invalid or disabled Google
      * Play Services installation via a user dialog, if possible.
      */
    public static void acquireGooglePlayServices(Context context) {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(context);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
           Log.e("Error","Connection Status Code"+connectionStatusCode);
        }
    }


}
