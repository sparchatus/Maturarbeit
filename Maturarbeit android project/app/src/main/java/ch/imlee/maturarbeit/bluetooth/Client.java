package ch.imlee.maturarbeit.bluetooth;

import ch.imlee.maturarbeit.settings.TestActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Intent;
import android.widget.AdapterView.OnItemClickListener;


import java.util.ArrayList;
import java.lang.reflect.Method;


public class Client{
    Context c;
    Util util = new Util();
    ArrayList<BluetoothDevice> devices;
    ArrayList<String> deviceNames;
    ArrayAdapter<String> adapter;

    public Client(Context context){
        c = context;
        util.initBluetooth(c);
        if(TestActivity.usernameEditText.getText().toString().endsWith("_HOST")){


                Toast.makeText(c, "Invalid Username", Toast.LENGTH_LONG).show();
                TestActivity.hostButton.setVisibility(View.VISIBLE);
                TestActivity.joinButton.setVisibility(View.VISIBLE);
                TestActivity.usernameTextView.setVisibility(View.VISIBLE);
                TestActivity.usernameEditText.setVisibility(View.VISIBLE);

                TestActivity.modeDependantText.setVisibility(View.INVISIBLE);
                TestActivity.listView.setVisibility(View.INVISIBLE);
                TestActivity.progressBar.setVisibility(View.INVISIBLE);

        }
        Util.ba.setName(TestActivity.usernameEditText.getText().toString());
        devices.clear();
        deviceNames.clear();



        findDevices();
    }


    private void findDevices() {
        devices = new ArrayList<>();
        deviceNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, deviceNames);
        TestActivity.listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        TestActivity.listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Util.ba.isDiscovering()){
                    c.unregisterReceiver(mReceiver);
                    Util.ba.cancelDiscovery();
                }
                TestActivity.progressBar.setVisibility(View.VISIBLE);
                TestActivity.modeDependantText.setText("connecting to " + deviceNames.get(position));
                connectToDevice(devices.get(position));
            }
        });


        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        c.registerReceiver(this.mReceiver, filter); // Don't forget to unregister during onDestroy

        TestActivity.listView.setBackgroundColor(Color.DKGRAY);

        Util.ba.startDiscovery();
        }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getName().endsWith("_HOST")){
                    if(!devices.contains(device)) {
                        devices.add(device);
                        deviceNames.add(device.getName().substring(0, device.getName().length() - 5));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(c, "host found: " + deviceNames.get(deviceNames.size() - 1), Toast.LENGTH_SHORT).show();
                    }
                }
            } else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                TestActivity.modeDependantText.setText("discovering devices...");
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                TestActivity.modeDependantText.setText("discovery finished. " + devices.size() + " devices found.");
                TestActivity.progressBar.setVisibility(View.GONE);
            }
        }
    };
    private void connectToDevice(BluetoothDevice btDevice){
        if(btDevice.getBondState()!=BluetoothDevice.BOND_BONDED){
            try {
                Method method = btDevice.getClass().getMethod("createBond", (Class[]) null);
                method.invoke(btDevice, (Object[]) null);
            } catch (Exception e) {
               e.printStackTrace();
            }
        Toast.makeText(c, "connecting to " + btDevice.getName(), Toast.LENGTH_SHORT).show();
        }
        while(btDevice.getBondState()==BluetoothDevice.BOND_BONDING){
            try {
                Thread.sleep(50);
            } catch(Exception e){
                // Doesn't matter, continue
            }
        }
        Toast.makeText(c, "connected to " + btDevice.getName(), Toast.LENGTH_SHORT);
    }

}