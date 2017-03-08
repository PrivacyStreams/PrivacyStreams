package com.github.privacystreams.device;

import android.bluetooth.BluetoothDevice;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by Mingquan Liu 2017/3/6.
 */

public class BTDevice extends Item {
    private static final String DEVICENAME = "devicename";          // The device name for the bluetooth device
    private static final String MACAD = "macad";                      // The mac address for the bluetooth device
    private static final String BONDED = "bonded";                     // The bonded information for the bluetooth device

    BTDevice(BluetoothDevice scannedDevice){
        this.setFieldValue(DEVICENAME, scannedDevice.getName());
        this.setFieldValue(MACAD,scannedDevice.getAddress());
        this.setFieldValue(BONDED,scannedDevice.getBondState());
    }
    public static MultiItemStreamProvider asUpdates(){
        return new BlueToothUpdatesProvider();
    }
}
