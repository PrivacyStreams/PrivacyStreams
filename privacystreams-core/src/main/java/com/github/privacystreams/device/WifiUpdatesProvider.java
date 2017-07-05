package com.github.privacystreams.device;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * provides wifi information through returning a WifiAp
 * fires while connects to or disconnects from a wifi connection
 */
public class WifiUpdatesProvider extends MStreamProvider {
    private WifiAp oldWifiOutput;
    private WifiUpdatesProvider.WifiUpdatesReceiver wifiUpdatesReceiver;

    public WifiUpdatesProvider() {
        this.addRequiredPermissions(Manifest.permission.ACCESS_WIFI_STATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void provide() {
        oldWifiOutput = null;
        wifiUpdatesReceiver
                = new WifiUpdatesProvider.WifiUpdatesReceiver();
        IntentFilter filter = new IntentFilter();
        //Network State Changed Action has firing problem while device disconnects from wifi
        //Connectivity Action has problem in telling whether the change is due to wifi connection
        //or mobile connection. Hence these two actions are combined here.
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(wifiUpdatesReceiver, filter);
        //get connection information if the device is connected to wifi for the first time
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext()
                .getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                oldWifiOutput = getChangedWifi();
            }
        }
    }

    @Override
    protected void onCancel(UQI uqi) {
        this.getContext().unregisterReceiver(wifiUpdatesReceiver);
    }

    private class WifiUpdatesReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void onReceive(Context context, Intent intent) {
            //from disconnected to connected
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo networkInfo =
                        intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                WifiManager wifiManager
                        = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if (networkInfo != null && wifiManager.getConnectionInfo().getBSSID() != null) {
                    if (networkInfo.isConnected()) {
                        WifiAp wifiOutput = getChangedWifi();
                        oldWifiOutput = wifiOutput;
                        wifiOutput.setFieldValue(WifiAp.STATUS, WifiAp.STATUS_CONNECTED);
                        WifiUpdatesProvider.this.output(wifiOutput);
                    }
                }
            }
            //from connected to disconnected
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getContext()
                        .getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null && oldWifiOutput != null) {
                    oldWifiOutput.setFieldValue(WifiAp.STATUS, WifiAp.STATUS_DISCONNECTED);
                    WifiUpdatesProvider.this.output(oldWifiOutput);
                }
                if (networkInfo != null) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
                            !networkInfo.isConnected()) {
                        oldWifiOutput.setFieldValue(WifiAp.STATUS, WifiAp.STATUS_DISCONNECTED);
                        WifiUpdatesProvider.this.output(oldWifiOutput);
                    }
                }
            }
        }
    }

    /**
     * this method is used to return the current connection information
     *
     * @return wifiAp return the connection information
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private WifiAp getChangedWifi() {
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        return new WifiAp(wifiManager.getConnectionInfo(), WifiAp.STATUS_SCANNED);
    }
}


