package com.github.privacystreams.device;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by Mingquan Liu 2017/3/6.
 */

public class BluetoothDevice extends Item {

    private static final String NAME = "name";          // The device name for the bluetooth device
    private static final String MAC_ADDRESS = "mac_address";                      // The mac address for the bluetooth device
    private static final String BONDED = "bonded";                     // The bonded information for the bluetooth device

    BluetoothDevice(android.bluetooth.BluetoothDevice scannedDevice){
        this.setFieldValue(NAME, scannedDevice.getName());
        this.setFieldValue(MAC_ADDRESS, scannedDevice.getAddress());
        this.setFieldValue(BONDED, scannedDevice.getBondState());
    }

    public static MultiItemStreamProvider asUpdates(){
        return new BluetoothUpdatesProvider();
    }
}
