package ch.imlee.maturarbeit.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;

import ch.imlee.maturarbeit.settings.TestActivity;


/**
 * Created by Lukas on 09.04.2015.
 */
public class Util extends Activity{
    public static BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
    public TestActivity testActivity = new TestActivity();



    public void initBluetooth() {



        if (ba == null) {

            new AlertDialog.Builder(new Activity())
                    .setTitle("Error")
                    .setMessage("Your Device seems to not have Bluetooth")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(1);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        enableBluetooth();
    }


    public void enableBluetooth() {

        if (!ba.isEnabled()) {
            ba.enable();
        }
    }

    public void discoverDevices(){
        ba.startDiscovery();
    }
    // TODO when game starts, disable discovery
    public void cancelDiscovery(){
        ba.cancelDiscovery();
    }

}
