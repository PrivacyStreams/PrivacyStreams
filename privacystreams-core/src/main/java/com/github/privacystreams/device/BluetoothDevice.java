package com.github.privacystreams.device;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A BluetoothDevice represents a bluetooth device.
 */
@PSItem
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

    /**
     * Get a stream of scanned bluetooth devices.
     * This provider requires `android.permission.BLUETOOTH` permission
     * and `android.permission.BLUETOOTH_ADMIN` permission.
     *
     * @return the provider function.
     */
    // @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN})
    public static MStreamProvider getScanResults() {
        return new BluetoothDeviceListProvider();
    }
}
