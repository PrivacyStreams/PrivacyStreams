package io.github.privacystreams.device;

import android.Manifest;

import io.github.privacystreams.core.Function;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.DeviceUtils;

/**
 * Get device id
 */
class WifiStatusChecker extends Function<Void, Boolean> {

    WifiStatusChecker() {
        this.addRequiredPermissions(Manifest.permission.ACCESS_WIFI_STATE);
    }

    @Override
    public Boolean apply(UQI uqi, Void input) {
        return DeviceUtils.isWifiConnected(uqi.getContext());
    }
}
