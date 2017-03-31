package com.github.privacystreams.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import com.github.privacystreams.core.providers.MStreamProvider;

/**
 * Provide a stream of device state updates, including screen, boot, battery, ringer, etc.
 */

class DeviceEventUpdatesProvider extends MStreamProvider {

    class DeviceStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String event = null;
            String type = null;

            switch(intent.getAction()){
                case Intent.ACTION_SCREEN_OFF:
                    event = DeviceEvent.EVENT_SCREEN_OFF;
                    type = DeviceEvent.TYPE_SCREEN;
                    break;

                case Intent.ACTION_SCREEN_ON:
                    event = DeviceEvent.EVENT_SCREEN_ON;
                    type = DeviceEvent.TYPE_SCREEN;
                    break;

                case Intent.ACTION_USER_PRESENT:
                    event = DeviceEvent.EVENT_SCREEN_USER_PRESENT;
                    type = DeviceEvent.TYPE_SCREEN;
                    break;

                case Intent.ACTION_BOOT_COMPLETED:
                    event = DeviceEvent.EVENT_BOOT_COMPLETED;
                    type = DeviceEvent.TYPE_BOOT;
                    break;

                case Intent.ACTION_SHUTDOWN:
                    event = DeviceEvent.EVENT_BOOT_SHUTDOWN;
                    type = DeviceEvent.TYPE_BOOT;
                    break;

                case Intent.ACTION_BATTERY_LOW:
                    event = DeviceEvent.EVENT_BATTERY_LOW;
                    type = DeviceEvent.TYPE_BATTERY;
                    break;

                case Intent.ACTION_BATTERY_OKAY:
                    event = DeviceEvent.EVENT_BATTERY_OKAY;
                    type = DeviceEvent.TYPE_BATTERY;
                    break;

                case Intent.ACTION_POWER_CONNECTED:
                    event = DeviceEvent.EVENT_BATTERY_AC_CONNECTED;
                    type = DeviceEvent.TYPE_BATTERY;
                    break;

                case Intent.ACTION_POWER_DISCONNECTED:
                    event = DeviceEvent.EVENT_BATTERY_AC_DISCONNECTED;
                    type = DeviceEvent.TYPE_BATTERY;
                    break;

                case AudioManager.RINGER_MODE_CHANGED_ACTION:
                    AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
                    switch (am.getRingerMode()) {
                        case AudioManager.RINGER_MODE_SILENT:
                            event = DeviceEvent.EVENT_RINGER_SILENT;
                            type = DeviceEvent.TYPE_RINGER;
                            break;

                        case AudioManager.RINGER_MODE_VIBRATE:
                            event = DeviceEvent.EVENT_RINGER_VIBRATE;
                            type = DeviceEvent.TYPE_RINGER;
                            break;

                        case AudioManager.RINGER_MODE_NORMAL:
                            event = DeviceEvent.EVENT_RINGER_NORMAL;
                            type = DeviceEvent.TYPE_RINGER;
                            break;
                    }
                default:
                    break;
            }

            if (type != null)
                output(new DeviceEvent(System.currentTimeMillis(), type, event));
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
