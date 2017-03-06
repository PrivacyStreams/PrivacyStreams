package com.github.privacystreams.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by fanglinchen on 2/19/17.
 */

public class DeviceStateUpdatesProvider extends MultiItemStreamProvider {

     class DeviceStateReceiver extends BroadcastReceiver {
        private int state;
        @Override
        public void onReceive(Context context, Intent intent) {

            switch(intent.getAction()){
                case Intent.ACTION_SCREEN_OFF:
                    state = DeviceState.SCREEN_LOCK;
                    break;
                case Intent.ACTION_SCREEN_ON:
                    state = DeviceState.SCREEN_ON;
                    break;
                case Intent.ACTION_USER_PRESENT:
                    state = DeviceState.SCREEN_UNLOCK;
                    break;
                case Intent.ACTION_BOOT_COMPLETED:
                    state = DeviceState.POWER_ON;
                    break;
                case Intent.ACTION_SHUTDOWN:
                    state = DeviceState.POWER_OFF;
                    break;
                case Intent.ACTION_BATTERY_LOW:
                    state = DeviceState.BATTERY_LOW;
                    break;
                case Intent.ACTION_POWER_CONNECTED:
                    state = DeviceState.AC_CONNECTED;
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    state = DeviceState.AC_DISCONNECTED;
                    break;
                default:
                    break;
            }
            output(new DeviceState(System.currentTimeMillis(),state));

        }
    }

    @Override
    protected void provide() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_SHUTDOWN);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        DeviceStateReceiver mReceiver = new DeviceStateReceiver();
        getContext().registerReceiver(mReceiver, filter);
    }

}
