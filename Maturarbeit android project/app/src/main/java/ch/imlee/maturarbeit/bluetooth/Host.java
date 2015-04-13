package ch.imlee.maturarbeit.bluetooth;

import android.app.IntentService;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import ch.imlee.maturarbeit.settings.TestActivity;

/**
 * Created by Lukas on 08.04.2015.
 */
public class Host{
    TestActivity testActivity = new TestActivity();
    Util util = new Util();
    public Host(){
        util.initBluetooth();
        util.ba.setName(TestActivity.usernameEditText.getText().toString()+"_HOST");

        util.discoverDevices();

        // wait until discovered
        // TODO when game starts, disable discovery

    }
}
