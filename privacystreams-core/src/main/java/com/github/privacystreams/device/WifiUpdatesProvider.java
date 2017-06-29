package com.github.privacystreams.device;

import android.Manifest;

import com.github.privacystreams.core.providers.MStreamProvider;

/**
 * provides wifi status will connect and disconnect
 */
//Todo: to be complete
public class WifiUpdatesProvider extends MStreamProvider {
    public WifiUpdatesProvider(){
        this.addRequiredPermissions(Manifest.permission.ACCESS_WIFI_STATE);
    }
    @Override
    protected void provide(){

    }
}
