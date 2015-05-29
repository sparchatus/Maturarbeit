package ch.imlee.maturarbeit.bluetooth;

import ch.imlee.maturarbeit.settings.TestActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
    BluetoothSocket socket;
    String deviceName;

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
                try {
                    c.unregisterReceiver(mReceiver);
                } catch (Exception e) {
                    //ignore, probably already unregistered
                }
                if (Util.ba.isDiscovering()) {

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

        Util.ba.cancelDiscovery();

        socket = null;
        try{
            //Method m = btDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
            //socket = (BluetoothSocket) m.invoke(btDevice, 1);
            socket = btDevice.createRfcommSocketToServiceRecord(Util.generateUUID());

            // the generated UUID contains the version name and code, so only players with the same game version can play together.
            // Todo: doesn't work anymore, workaround needed.
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

            System.out.println("");
            System.out.println("attempting to connect...");
            System.out.println("");
            try {
                socket.connect();
                System.out.println(socket.getRemoteDevice().getBondState());
                System.out.println(Util.ba.getState());
                //break;
                //    Toast.makeText(c, "connection successful", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(c, "connection failed", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
                //try again
            }

            deviceName = socket.getRemoteDevice().getName().substring(0,socket.getRemoteDevice().getName().length()-5);
        try {
            Util.inputStream = socket.getInputStream();
            Util.outputStream = socket.getOutputStream();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        Util.sendString("hello " + deviceName + ", I'm " + Util.ba.getName());

        Toast.makeText(c, "connected to " + btDevice.getName().substring(0,btDevice.getName().length()-5), Toast.LENGTH_SHORT).show();
    }

}