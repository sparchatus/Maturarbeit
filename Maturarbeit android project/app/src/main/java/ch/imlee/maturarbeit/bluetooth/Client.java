package ch.imlee.maturarbeit.bluetooth;

import ch.imlee.maturarbeit.settings.TestActivity;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.bluetooth.BluetoothDevice;
import android.widget.Toast;
import android.content.IntentFilter;

import java.util.ArrayList;

/**
 * Created by Lukas on 08.04.2015.
 */
public class Client extends BroadcastReceiver{
    ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();

    Util util = new Util();
    public Client(){
        util.initBluetooth();

        util.ba.setName(TestActivity.usernameEditText.getText().toString());
        util.discoverDevices();
        findDevices();
    }

    private void findDevices(){
        BroadcastReceiver receiver = new SingBroadcastReceiver();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
   //     registerReceiver(receiver, filter);
//TODO fix dat shit
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    private class SingBroadcastReceiver extends BroadcastReceiver {


        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a Toast
                if(device.getName().endsWith("_HOST")){
                    devices.add(device);
                    Toast.makeText(context, "found Device: " + device.getName().substring(0,device.getName().length()-5), Toast.LENGTH_LONG).show();
                    refreshListView();
                }

            }
        }
        public void refreshListView(){

        }
    }
}
