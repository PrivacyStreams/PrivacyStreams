package com.github.privacystreams.device;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;

/**
 * Provide a stream of current visible bluetooth device list.
 */

class BluetoothDeviceListProvider extends MStreamProvider {
    private int count;                      // count how many devices have been checked
    private BluetoothAdapter BTAdapter;
    private IntentFilter intentFilter;

    // Sets up the permission requirement
    BluetoothDeviceListProvider() {
        this.addRequiredPermissions(Manifest.permission.BLUETOOTH);
        this.addRequiredPermissions(Manifest.permission.BLUETOOTH_ADMIN);
    }

    @Override
    protected void provide() {
        BTAdapter = BluetoothAdapter.getDefaultAdapter();               // Set up the adaptor
        intentFilter = new IntentFilter();
        intentFilter.addAction(android.bluetooth.BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getContext().registerReceiver(mReceiver, intentFilter);
        BTAdapter.startDiscovery();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (android.bluetooth.BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                android.bluetooth.BluetoothDevice device = intent.getParcelableExtra(android.bluetooth.BluetoothDevice.EXTRA_DEVICE);
                // return the new bluetooth device
                output(new BluetoothDevice(device));
            }
        }
    };

    @Override
    protected void onCancelled(UQI uqi) {
        super.onCancelled(uqi);
        getContext().unregisterReceiver(mReceiver);
    }
}