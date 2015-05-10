package ch.imlee.maturarbeit.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.bluetooth.BluetoothSocket;

import ch.imlee.maturarbeit.settings.TestActivity;


/**
 * Created by Lukas on 09.04.2015.
 */
public class Util extends Activity{
    public static BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
    public void initBluetooth() {



        if (ba == null) {
        }else {
            enableBluetooth();
        }
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
