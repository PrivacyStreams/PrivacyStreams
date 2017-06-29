package com.github.privacystreams.device;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;

/**
 * provides wifi status will connect and disconnect
 */
//Todo: to be complete
public class WifiUpdatesProvider extends MStreamProvider {
    WifiUpdatesReceiver wifiUpdatesReceiver;
    UQI uqi;
    public WifiUpdatesProvider(){
        this.addRequiredPermissions(Manifest.permission.ACCESS_WIFI_STATE);
    }
    @Override
    protected void provide(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        getContext().registerReceiver(wifiUpdatesReceiver, filter);
    }

    private class WifiUpdatesReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            final String action = intent.getAction();
            if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
                if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){

                } else {

                }
            }
        }
    }
}
