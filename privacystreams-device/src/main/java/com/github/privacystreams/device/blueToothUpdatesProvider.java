package com.github.privacystreams.device;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.Activity;
import android.util.Log;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.MultiItemStreamProvider;

/**
 * Created by lenovo on 2017/3/6.
 */

public class blueToothUpdatesProvider extends MultiItemStreamProvider{
    private int count;                      // count how many devices have been checked
    private BluetoothAdapter BTAdapter;
    private IntentFilter intentFilter;

    // Sets up the permission requirement
    blueToothUpdatesProvider() {
        this.addRequiredPermissions(Manifest.permission.BLUETOOTH);
        this.addRequiredPermissions(Manifest.permission.BLUETOOTH_ADMIN);
    }

    @Override
    protected void provide() {
        Log.e("BTProvider","Starts");
        BTAdapter = BluetoothAdapter.getDefaultAdapter();               // Set up the adaptor
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        count = 0;
        getContext().registerReceiver(mReceiver, intentFilter);
        BTAdapter.startDiscovery();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Log.e("BTProvider","Here");
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e("BTProvider","Find One");
                count++;
                Log.e("BT", "Name: " + device.getName() + " Mac Address: " + device.getAddress() + " Bonded: " + device.getBondState());
                output(new BTDevice(device));                       // return the new bluetooth device
                // If it's already paired, skip it, because it's been listed already
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.e("BTProvider","Finish Finding");
                if (count == 0) {
                    Log.e("BT", "No blueTooth device was found.");
                }
            }
        }
    };

    @Override
    protected void onCancelled(UQI uqi) {
        super.onCancelled(uqi);
        getContext().unregisterReceiver(mReceiver);
    }

}