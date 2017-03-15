package com.github.privacystreams.device;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;


/**
 * Created by fanglinchen on 1/31/17.
 * TODO carefully define what is Wifi updates.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
class WifiUpdatesProvider extends MStreamProvider {
    private transient WifiManager wifi;
    private final int samplingPeriodInSeconds;

    class WifiReceiver extends  BroadcastReceiver
    {
        @Override
        public void onReceive(Context c, Intent intent)
        {
            WifiManager wifiMgr = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            String name = wifiInfo.getSSID();
            for(ScanResult result: wifi.getScanResults()){
                output(new WifiAp(result,name.equals(result.SSID)));
            }
        }
    }


    @Override
    protected void onCancelled(UQI uqi) {
        super.onCancelled(uqi);
        getContext().unregisterReceiver(wifiReceiver);
    }

    private transient Handler handler;
    private transient WifiReceiver wifiReceiver;

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            wifi.startScan();
            // Repeat this the same runnable code block again another 2 seconds
            handler.postDelayed(this, samplingPeriodInSeconds*1000);
        }
    };


    public WifiUpdatesProvider(int samplingPeriodInSeconds) {
        this.samplingPeriodInSeconds = samplingPeriodInSeconds;
        this.addParameters(samplingPeriodInSeconds);
        this.addRequiredPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE);
    }


    @Override
    protected void provide() {

        wifi = (WifiManager) this.getContext().
                getSystemService(Context.WIFI_SERVICE);

        Looper.prepare();
        handler =  new Handler();
        wifiReceiver = new WifiReceiver();

        wifi.startScan();

        handler.postDelayed(runnableCode, samplingPeriodInSeconds*1000);

        if(wifi.isWifiEnabled())
        {
            this.getContext().registerReceiver(wifiReceiver,
                    new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        }

    }

}
