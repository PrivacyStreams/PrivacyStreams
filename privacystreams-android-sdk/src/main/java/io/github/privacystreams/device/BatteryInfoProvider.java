package io.github.privacystreams.device;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import io.github.privacystreams.core.PStreamProvider;

/**
 * Provide a single item of current battery info.
 */
class BatteryInfoProvider extends PStreamProvider {

    private float getBatteryLevel() {
        Intent batteryIntent = this.getContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent != null ? batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : 0;
        int scale = batteryIntent != null ? batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : 0;

        return ((float) level / (float) scale) * 100.0f;
    }

    @Override
    protected void provide() {
        output(new BatteryInfo(System.currentTimeMillis(), getBatteryLevel()));
        this.finish();
    }
}
