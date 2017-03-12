package com.github.privacystreams.device;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.sql.Time;

/**
 * A BluetoothDevice item represents the information of a scanned Bluetooth device.
 */
@PSItem
public class BluetoothDevice extends Item {

    /**
     * The device name for the bluetooth device.
     */
    @PSItemField(type = String.class)
    private static final String NAME = "name";

    /**
     * The mac address for the bluetooth device.
     */
    @PSItemField(type = String.class)
    private static final String MAC_ADDRESS = "mac_address";

    //
    /**
     * The status information indicating whether the bluetooth device is bonded
     * to the phone.
     */
    @PSItemField(type = Boolean.class)
    private static final String BONDED = "bonded";

    /**
     * The timestamp of when the WIFI AP information is found.
     */
    @PSItemField(type = Long.class)
    private static final String TIMESTAMP = "timestamp";

    BluetoothDevice(android.bluetooth.BluetoothDevice scannedDevice){
        this.setFieldValue(NAME, scannedDevice.getName());
        this.setFieldValue(MAC_ADDRESS, scannedDevice.getAddress());
        this.setFieldValue(BONDED, scannedDevice.getBondState());
        this.setFieldValue(TIMESTAMP, System.currentTimeMillis());
    }

    public static Function<Void, MultiItemStream> asUpdates(){
        return new BluetoothDeviceListProvider();
    }
}
