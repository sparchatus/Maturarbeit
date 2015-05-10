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
    Util util = new Util();
    public Host(Context c){
        util.initBluetooth(c);
        Util.ba.setName(TestActivity.usernameEditText.getText().toString() + "_HOST");
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        c.startActivity(discoverableIntent);
    }
}
