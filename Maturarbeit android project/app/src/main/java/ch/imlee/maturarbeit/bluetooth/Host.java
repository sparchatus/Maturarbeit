package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ch.imlee.maturarbeit.settings.TestActivity;

public class Host{

    Util util = new Util();
    Context c;
    ArrayList<BluetoothDevice> devices = new ArrayList<>();
    ArrayAdapter<BluetoothDevice> adapter = new ArrayAdapter<BluetoothDevice>(c, android.R.layout.simple_list_item_1, devices);

    public Host(Context context){
        c = context;
        util.initBluetooth(c);
        Util.ba.setName(TestActivity.usernameEditText.getText().toString() + "_HOST");
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        c.startActivity(discoverableIntent);

        TestActivity.listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        

    }
}






