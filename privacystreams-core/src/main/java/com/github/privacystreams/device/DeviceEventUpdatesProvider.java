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
                    event = DeviceEvent.ScreenEvents.OFF;
                    type = DeviceEvent.Types.SCREEN;
                    break;

                case Intent.ACTION_SCREEN_ON:
                    event = DeviceEvent.ScreenEvents.ON;
                    type = DeviceEvent.Types.SCREEN;
                    break;

                case Intent.ACTION_USER_PRESENT:
                    event = DeviceEvent.ScreenEvents.USER_PRESENT;
                    type = DeviceEvent.Types.SCREEN;
                    break;

                case Intent.ACTION_BOOT_COMPLETED:
                    event = DeviceEvent.BootEvents.BOOT_COMPLETED;
                    type = DeviceEvent.Types.BOOT;
                    break;

                case Intent.ACTION_SHUTDOWN:
                    event = DeviceEvent.BootEvents.SHUTDOWN;
                    type = DeviceEvent.Types.BOOT;
                    break;

                case Intent.ACTION_BATTERY_LOW:
                    event = DeviceEvent.BatteryEvents.LOW;
                    type = DeviceEvent.Types.BATTERY;
                    break;

                case Intent.ACTION_BATTERY_OKAY:
                    event = DeviceEvent.BatteryEvents.OKAY;
                    type = DeviceEvent.Types.BATTERY;
                    break;

                case Intent.ACTION_POWER_CONNECTED:
                    event = DeviceEvent.BatteryEvents.AC_CONNECTED;
                    type = DeviceEvent.Types.BATTERY;
                    break;

                case Intent.ACTION_POWER_DISCONNECTED:
                    event = DeviceEvent.BatteryEvents.AC_DISCONNECTED;
                    type = DeviceEvent.Types.BATTERY;
                    break;

                case AudioManager.RINGER_MODE_CHANGED_ACTION:
                    AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
                    switch (am.getRingerMode()) {
                        case AudioManager.RINGER_MODE_SILENT:
                            event = DeviceEvent.RingerEvents.SILENT;
                            type = DeviceEvent.Types.RINGER;
                            break;

                        case AudioManager.RINGER_MODE_VIBRATE:
                            event = DeviceEvent.RingerEvents.VIBRATE;
                            type = DeviceEvent.Types.RINGER;
                            break;

                        case AudioManager.RINGER_MODE_NORMAL:
                            event = DeviceEvent.RingerEvents.NORMAL;
                            type = DeviceEvent.Types.RINGER;
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
