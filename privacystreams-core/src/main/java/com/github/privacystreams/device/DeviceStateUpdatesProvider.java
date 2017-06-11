package com.github.privacystreams.device;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.core.purposes.Purpose;

import java.util.List;

/**
 * Provide a live stream of device states.
 */
class DeviceStateUpdatesProvider extends MStreamProvider {
    private long frequency;
    private int mask;

    DeviceStateUpdatesProvider(long frequency, int mask){
        this.frequency = frequency;
        this.mask = mask;
        this.addParameters(frequency, mask);
    }

    @Override
    protected void provide() {

        while(true){
            if(!isCancelled){
                Item deviceState = new Item();
                if ((mask & DeviceState.Masks.BLUETOOTH_DEVICE_LIST) != 0) {
                    getBluetoothDeviceList(deviceState);
                }

                if ((mask & DeviceState.Masks.WIFI_AP_LIST) != 0) {
                    getWifiList(deviceState);
                }

                if ((mask& DeviceState.Masks.BATTERY_LEVEL)!=0){
                    getBatteryInfo(deviceState);
                }
                output(deviceState);

                try {
                    Thread.sleep(frequency);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getBatteryInfo(Item deviceState){
        try {
            float level = this.getUQI()
                    .getData(BatteryInfo.asSnapshot(), Purpose.LIB_INTERNAL("BatteryInfoProvider"))
                    .getField(BatteryInfo.LEVEL);

            deviceState.setFieldValue(DeviceState.BATTERY_LEVEL, level);
        } catch (PSException e) {
            e.printStackTrace();
        }
    }

    private void getBluetoothDeviceList(Item deviceState) {
        try {
            List<Item> bluetoothList = this.getUQI()
                    .getData(BluetoothDevice.getScanResults(), Purpose.LIB_INTERNAL("DeviceStateUpdatesProvider"))
                    .asList();
            deviceState.setFieldValue(DeviceState.BLUETOOTH_DEVICE_LIST, bluetoothList);
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
