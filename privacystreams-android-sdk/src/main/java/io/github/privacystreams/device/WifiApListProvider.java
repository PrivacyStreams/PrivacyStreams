package io.github.privacystreams.device;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

<<<<<<< HEAD:privacystreams-core/src/main/java/com/github/privacystreams/device/WifiApListProvider.java
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.Logging;
=======
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.PStreamProvider;
>>>>>>> eb641f8ad850f8242057d9884a6ce35f8fd5ea8f:privacystreams-android-sdk/src/main/java/io/github/privacystreams/device/WifiApListProvider.java


/**
 * Provide a stream of current WIFI AP list.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
class WifiApListProvider extends PStreamProvider {

    class WifiReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            WifiManager wifiMgr = (WifiManager) context.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            String name = wifiInfo.getBSSID();
            for(ScanResult result: wifiMgr.getScanResults()){

                if(name.equals(result.BSSID)){
                    WifiApListProvider.this.output(new WifiAp(result, WifiAp.STATUS_CONNECTED));
                }
                else{
                    WifiApListProvider.this.output(new WifiAp(result,WifiAp.STATUS_SCANNED));
                }
            }
            WifiApListProvider.this.finish();
        }
    }

    @Override
    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        if(wifiReceiver!=null){
            try{
                getContext().unregisterReceiver(wifiReceiver);
            }
            catch (IllegalArgumentException exception){
                Logging.warn("The following exception has been thrown: "+exception.getMessage());
            }
        }

    }

    private transient WifiReceiver wifiReceiver;

    WifiApListProvider() {
        this.addRequiredPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE);
        this.wifiReceiver = new WifiReceiver();
    }


    @Override
    protected void provide() {

        WifiManager wifiMgr = (WifiManager) this.getContext().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        if(wifiMgr.isWifiEnabled()) {
            this.getContext().registerReceiver
                    (this.wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            wifiMgr.startScan();
        }
        else{
            this.finish();
        }

    }

}
