package com.github.privacystreams.device;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MStream;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * Created by Mingquan Liu 2017/3/6.
 */

public class BluetoothDevice extends Item {
    /**
     * The name of the scanned bluetooth device.
     */
    @PSItemField(type = String.class)
    public static final String NAME = "name";

    /**
     * The mac address of the scanned bluetooth device.
     */
    @PSItemField(type = String.class)
    public static final String MAC_ADDRESS = "mac_address";

    /**
     * The boolean value indicating whether the bluetooth device is connected to the user's phone.
     */
    @PSItemField(type = Boolean.class)
    public static final String BONDED = "bonded";

    BluetoothDevice(android.bluetooth.BluetoothDevice scannedDevice){
        this.setFieldValue(NAME, scannedDevice.getName());
        this.setFieldValue(MAC_ADDRESS, scannedDevice.getAddress());
        this.setFieldValue(BONDED, scannedDevice.getBondState());
    }

    public static Function<Void, MStream> asUpdates(){
        return new BluetoothDeviceListProvider();
    }
}