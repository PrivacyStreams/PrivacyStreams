package com.github.privacystreams.device;

import android.Manifest;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.UQI;

/**
 * Get device id
 */
class DeviceIdGetter extends Function<Void, String> {

    DeviceIdGetter() {
        this.addRequiredPermissions(Manifest.permission.READ_PHONE_STATE);
    }

    private transient String uuid;

    @Override
    public String apply(UQI uqi, Void input) {
        if (this.uuid == null) {
            TelephonyManager tm = (TelephonyManager) uqi.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            this.uuid = tm.getDeviceId();
        }
        return this.uuid;
    }
}
