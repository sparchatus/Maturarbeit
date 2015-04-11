package ch.imlee.maturarbeit.bluetooth;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.Activity;

import ch.imlee.maturarbeit.settings.TestActivity;

/**
 * Created by Lukas on 09.04.2015.
 */
public class Util {
    public BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
    public Activity ta = new Activity();
    public void initBluetooth() {

        if (ba == null) {
            new AlertDialog.Builder(new TestActivity())
                    .setTitle("Error")
                    .setMessage("Your Device seems to not have Bluetooth")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                             return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        enableBluetooth();

    }

    public void enableBluetooth(){
        if(!ba.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ta.startActivity(enableBtIntent);
        }
    }

}
