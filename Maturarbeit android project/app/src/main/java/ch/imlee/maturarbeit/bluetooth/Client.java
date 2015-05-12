package ch.imlee.maturarbeit.bluetooth;

import ch.imlee.maturarbeit.settings.TestActivity;
import ch.imlee.maturarbeit.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Intent;
import android.os.Bundle;

import junit.framework.Test;

import java.util.ArrayList;


/**
 * Created by Lukas on 08.04.2015.
 */
public class Client{
   // ArrayList<BluetoothDevice> foundDevices = new ArrayList<BluetoothDevice>();
    Context ta;
    Context c;
    Util util = new Util();
    ArrayList<BluetoothDevice> devices;
    ArrayList<String> deviceNames;
    ArrayAdapter<String> adapter;

    public Client(Context context){
        c = context;
        util.initBluetooth(c);
        if(TestActivity.usernameEditText.getText().toString().endsWith("_HOST")){


                Toast.makeText(c, "Invalid Username", Toast.LENGTH_LONG).show();
                TestActivity.hostButton.setVisibility(View.VISIBLE);
                TestActivity.joinButton.setVisibility(View.VISIBLE);
                TestActivity.usernameTextView.setVisibility(View.VISIBLE);
                TestActivity.usernameEditText.setVisibility(View.VISIBLE);

                TestActivity.modeDependantText.setVisibility(View.INVISIBLE);
                TestActivity.listView.setVisibility(View.INVISIBLE);
                TestActivity.progressBar.setVisibility(View.INVISIBLE);

        }
        Util.ba.setName(TestActivity.usernameEditText.getText().toString());


        findDevices();
    }


    private void findDevices() {
        devices = new ArrayList<BluetoothDevice>();
        deviceNames = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, deviceNames);
        TestActivity.listView.setAdapter(adapter);


        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        c.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        TestActivity.modeDependantText.setText("hi");

        Util.ba.startDiscovery();
        }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            Toast.makeText(c, "onReceive", Toast.LENGTH_SHORT).show();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getName().endsWith("_HOST")){
                        devices.add(device);
                        deviceNames.add(device.getName().substring(0,device.getName().length()-6));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(c, "host found: " + deviceNames.get(deviceNames.size()-1), Toast.LENGTH_SHORT).show();
                }
            } else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                TestActivity.modeDependantText.setText("discovery started.");
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                TestActivity.modeDependantText.setText("discovery finished.");
            }
        }
    };

}