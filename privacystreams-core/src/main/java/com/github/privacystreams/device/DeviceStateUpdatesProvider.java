package com.github.privacystreams.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by fanglinchen on 2/19/17.
 */

class DeviceStateUpdatesProvider extends MultiItemStreamProvider {
    String TYPE_SCREEN = "screen";
    String TYPE_POWER = "power";
    String TYPE_RINGER = "ringer";

    class DeviceStateReceiver extends BroadcastReceiver {
        private String state;
        private String type;

        @Override
        public void onReceive(Context context, Intent intent) {

            switch(intent.getAction()){
                case Intent.ACTION_SCREEN_OFF:
                    state = DeviceStateChange.SCREEN_LOCK;
                    type = TYPE_SCREEN;
                    break;

                case Intent.ACTION_SCREEN_ON:
                    state = DeviceStateChange.SCREEN_ON;
                    type = TYPE_SCREEN;
                    break;

                case Intent.ACTION_USER_PRESENT:
                    state = DeviceStateChange.SCREEN_UNLOCK;
                    type = TYPE_SCREEN;
                    break;

                case Intent.ACTION_BOOT_COMPLETED:
                    state = DeviceStateChange.POWER_ON;
                    type = TYPE_POWER;
                    break;

                case Intent.ACTION_SHUTDOWN:
                    state = DeviceStateChange.POWER_OFF;
                    type = TYPE_POWER;
                    break;
                case Intent.ACTION_BATTERY_LOW:
                    state = DeviceStateChange.BATTERY_LOW;
                    type = TYPE_POWER;
                    break;

                case Intent.ACTION_BATTERY_OKAY:
                    state = DeviceStateChange.BATTERY_OK;
                    type = TYPE_POWER;
                    break;
                case Intent.ACTION_POWER_CONNECTED:
                    state = DeviceStateChange.AC_CONNECTED;
                    type = TYPE_POWER;
                    break;

                case Intent.ACTION_POWER_DISCONNECTED:
                    state = DeviceStateChange.AC_DISCONNECTED;
                    type = TYPE_POWER;
                    break;

                case AudioManager.RINGER_MODE_CHANGED_ACTION:
                    AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
                    switch (am.getRingerMode()) {
                        case AudioManager.RINGER_MODE_SILENT:
                            state = DeviceStateChange.RINGER_SILENT;
                            type = TYPE_RINGER;
                            break;

                        case AudioManager.RINGER_MODE_VIBRATE:
                            state = DeviceStateChange.RINGER_VIBRATE;
                            type = TYPE_RINGER;
                            break;

                        case AudioManager.RINGER_MODE_NORMAL:
                            state = DeviceStateChange.RINGER_NORMAL;
                            type = TYPE_RINGER;
                            break;
                    }
                default:
                    break;
            }
            output(new DeviceStateChange(System.currentTimeMillis(), type, state));
        }
    }

    @Override
    protected void provide() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        filter.addAction(Intent.ACTION_SHUTDOWN);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);

        DeviceStateReceiver mReceiver = new DeviceStateReceiver();
        getContext().registerReceiver(mReceiver, filter);
    }
}
