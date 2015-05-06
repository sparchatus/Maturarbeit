package ch.imlee.maturarbeit.bluetooth;

import ch.imlee.maturarbeit.settings.TestActivity;
import ch.imlee.maturarbeit.R;

import android.bluetooth.BluetoothDevice;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Intent;


/**
 * Created by Lukas on 08.04.2015.
 */
public class Client{
   // ArrayList<BluetoothDevice> foundDevices = new ArrayList<BluetoothDevice>();
    TestActivity ta = new TestActivity();
    Util util = new Util();
    ArrayAdapter<BluetoothDevice> devices;


    public Client(){

        util.initBluetooth();
        if(TestActivity.usernameEditText.getText().toString().endsWith("_HOST")){


                    Toast.makeText(ta.getApplicationContext(), "Invalid Username", Toast.LENGTH_LONG).show();

        }
        Util.ba.setName(TestActivity.usernameEditText.getText().toString());

        util.discoverDevices();
        findDevices();
    }


    private void findDevices(){
        devices = new ArrayAdapter<BluetoothDevice>(ta, R.layout.activity_test/*R.layout.adapter_list*/);
        Util.ba.startDiscovery();


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        ta.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver(){

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                devices.add(device);
            }
        }
    };

}
