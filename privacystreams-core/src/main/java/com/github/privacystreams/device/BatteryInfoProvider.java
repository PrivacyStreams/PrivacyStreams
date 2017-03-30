package com.github.privacystreams.device;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.github.privacystreams.core.providers.SStreamProvider;

/**
 * Provide a single item of current battery info.
 */
class BatteryInfoProvider extends SStreamProvider {

    private float getBatteryLevel() {
        Intent batteryIntent = this.getContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return ((float)level / (float)scale) * 100.0f;
    }

    @Override
    protected void provide() {
        output(new BatteryInfo(System.currentTimeMillis(),getBatteryLevel()));
        this.finish();
    }
}
