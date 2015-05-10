package ch.imlee.maturarbeit.bluetooth;

import ch.imlee.maturarbeit.settings.TestActivity;
import android.bluetooth.BluetoothDevice;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Intent;
import java.util.ArrayList;


/**
 * Created by Lukas on 08.04.2015.
 */
public class Client{
   // ArrayList<BluetoothDevice> foundDevices = new ArrayList<BluetoothDevice>();
    Context ta;
    Util util = new Util();
    ArrayList<BluetoothDevice> devices;

    public Client(Context c){
        ta = c;
        util.initBluetooth();
        if(TestActivity.usernameEditText.getText().toString().endsWith("_HOST")){


                    Toast.makeText(ta.getApplicationContext(), "Invalid Username", Toast.LENGTH_LONG).show();

        }
        Util.ba.setName(TestActivity.usernameEditText.getText().toString());

        util.discoverDevices();
        findDevices();
    }


    private void findDevices() {
        devices = new ArrayList<BluetoothDevice>();
        Util.ba.startDiscovery();


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        if (ta == null) System.out.println(0);
        if (mReceiver == null) System.out.println(1);
        if (filter == null) System.out.println(2);
        System.out.println(3);
     //   System.exit(0);
        ta.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    devices.add(device);
                }
            }
        }
    };

}
