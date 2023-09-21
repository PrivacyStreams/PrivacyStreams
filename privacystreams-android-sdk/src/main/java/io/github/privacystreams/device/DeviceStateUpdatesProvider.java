package io.github.privacystreams.device;

import android.os.Build;
import androidx.annotation.RequiresApi;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.core.purposes.Purpose;
import io.github.privacystreams.utils.DeviceUtils;

import java.util.List;

/**
 * Provide a live stream of device states.
 */
class DeviceStateUpdatesProvider extends PStreamProvider {
    private long frequency;
    private int mask;

    DeviceStateUpdatesProvider(long frequency, int mask){
        this.frequency = frequency;
        this.mask = mask;
        this.addParameters(frequency, mask);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void provide() {

        while (!isCancelled){
            Item deviceState = new Item();
            if ((mask & DeviceState.Masks.BLUETOOTH_DEVICE_LIST) != 0) {
                getBluetoothDeviceList(deviceState);
            }

            if ((mask & DeviceState.Masks.WIFI_AP_LIST) != 0) {
                getWifiList(deviceState);
            }

            if ((mask & DeviceState.Masks.BATTERY_LEVEL) !=0) {
                getBatteryInfo(deviceState);
            }

            if ((mask & DeviceState.Masks.SCREEN_STATE) != 0) {
                deviceState.setFieldValue(DeviceState.IS_SCREEN_ON, DeviceUtils.isDeviceInteractive(getContext()));
            }

            if ((mask & DeviceState.Masks.CONNECTION_INFO) != 0) {
                deviceState.setFieldValue(DeviceState.IS_CONNECTED, DeviceUtils.isDeviceOnline(getContext()));
                deviceState.setFieldValue(DeviceState.WIFI_BSSID, DeviceUtils.getWifiBSSID(getContext()));
            }

            output(deviceState);

            try {
                Thread.sleep(frequency);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void getBatteryInfo(Item deviceState){
        float level = DeviceUtils.getBatteryLevel(this.getContext());
        if (level >= 0)
            deviceState.setFieldValue(DeviceState.BATTERY_LEVEL, level);
    }

    private void getBluetoothDeviceList(Item deviceState) {
        try {
            List<Item> bluetoothList = this.getUQI()
                    .getData(BluetoothDevice.getScanResults(), Purpose.LIB_INTERNAL("DeviceStateUpdatesProvider"))
                    .asList();
            deviceState.setFieldValue(DeviceState.BT_DEVICE_LIST, bluetoothList);
        } catch (PSException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void getWifiList(Item deviceState) {
        try {
            List<Item> wifiList = this.getUQI()
                    .getData(WifiAp.getScanResults(), Purpose.LIB_INTERNAL("DeviceStateUpdatesProvider"))
                    .asList();
            deviceState.setFieldValue(DeviceState.WIFI_AP_LIST, wifiList);
        } catch (PSException e) {
            e.printStackTrace();
        }
    }
}
