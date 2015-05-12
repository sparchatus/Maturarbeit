package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

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
