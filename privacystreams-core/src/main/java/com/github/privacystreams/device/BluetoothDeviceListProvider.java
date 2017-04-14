package com.github.privacystreams.device;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MStreamProvider;

import static android.content.ContentValues.TAG;

/**
 * Provide a stream of current visible bluetooth device list.
 */

class BluetoothDeviceListProvider extends MStreamProvider {

    // Sets up the permission requirement
    BluetoothDeviceListProvider() {
        this.addRequiredPermissions(Manifest.permission.BLUETOOTH);
        this.addRequiredPermissions(Manifest.permission.BLUETOOTH_ADMIN);
    }

    @Override
    protected void provide() {
        BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();               // Set up the adaptor
        if (BTAdapter == null || !BTAdapter.isEnabled()) {
            this.finish();
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
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
                Log.e("bt device",device.getAddress());
                BluetoothDeviceListProvider.this.output(new BluetoothDevice(device));
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                Log.e(TAG,"Entered the Finished ");
                BluetoothDeviceListProvider.this.finish();
            }

        }
    };

    @Override
    protected void onCancel(UQI uqi) {
        super.onCancel(uqi);
        try {
            getContext().unregisterReceiver(mReceiver);
        }
        catch (Exception ignored) {
        }
    }
}
