package ch.imlee.maturarbeit.bluetooth;

import ch.imlee.maturarbeit.activities.StartActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Intent;
import android.widget.AdapterView.OnItemClickListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Client extends StartActivity {
    private static Context c;

    private static ArrayList<BluetoothDevice> devices; // the found devices are stored here
    private static ArrayList<String> deviceNames; // here are the names of the found devices
    private static ArrayAdapter<String> adapter; // the adapter links the deviceNames List with the ListView from the StartActivity

    private static boolean connecting = false;
    private static BluetoothSocket socket; // this socket is used to connect to a host
    private static BluetoothDevice device; // the host device is stored here

    public static InputStream inputStream; // the hosts InputStream
    public static OutputStream outputStream; // the hosts OutputStream

    private IntentFilter filter = new IntentFilter(); // this filters what the BroadcastReceiver should listen to

    private Runnable connectRunnable = new Runnable() {
        @Override
        public void run() {
            // we need a BroadcastReceiver to catch the Intents sent when the Bluetooth discovery
            // starts and finishes, when a device is discovered and when the connection is successful
            try {
                c.registerReceiver(mReceiver, filter);
            } catch(Exception e){
                // probably already registered
            }
            connecting = true;
            Log.d("bluetooth", "attempting to connect");

            // because the connection doesn't always work at the first try
            final int MAX_ATTEMPTS = 2;
            boolean connectionSuccessful = false;
            breakpoint:
            // we need this for-loop to try every UUID, because we don't know to which the Host is
            // listening at the moment
            for(int j = 0; j < 7; ++j) {
                try{
                    socket = device.createRfcommSocketToServiceRecord(Util.getUUID(j));
                    // the generated UUID contains the version name and code,
                    // so only players with the same game version can play together.
                } catch (Exception e){
                    e.printStackTrace();
                }
                // sometimes random exceptions get thrown, so try every UUID twice
                for (int i = 0; i < MAX_ATTEMPTS; ++i) {
                    try {
                        socket.connect();
                        connectionSuccessful = true;
                        // the connection was successful, so break out of the outer for-loop
                        break breakpoint;
                    } catch (Exception e) {
                        Log.d("bluetooth", "connection attempt " + (i + 1) + '/' + MAX_ATTEMPTS +
                                " failed with UUID " + Util.getUUID(j));
                        e.printStackTrace();
                        //try again
                    }
                }
            }
            connecting = false;
            if(!connectionSuccessful){
                // if the connection was unsuccessful, display an error message via a Toast
                Client.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                        Toast.makeText(c, "can't connect to that device :(", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            Log.d("bluetooth", "connection successful");

            c.sendBroadcast(new Intent("connectionFinished"));
        }
    };

    // used to check whether device discovery finished automatically or was canceled
    private static boolean discoveryCanceled = false;



    public Client(Context context){
        c = context;

        Util.ba.setName(usernameEditText.getText().toString());

        devices = new ArrayList<>();
        deviceNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, deviceNames);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // if the user clicked on an entry on the ListView, connect to that device
                if (Util.ba.isDiscovering()) {
                    discoveryCanceled = true;
                    Util.ba.cancelDiscovery();
                }
                progressBar.setVisibility(View.VISIBLE);
                statusText.setText("connecting to " + deviceNames.get(position));
                connectToDevice(devices.get(position));
            }
        });

        // adding the actions to which the BroadcastReceiver should listen to
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction("connectionFinished");

        // activate the BroadcastReceiver
        c.registerReceiver(this.mReceiver, filter);

        // let the BluetoothAdapter discover devices
        Util.ba.startDiscovery();
    }

    public void connectToDevice(BluetoothDevice btDevice){
        if(connecting) return;
        device = btDevice;

        // cancel the discovery of more devices
        discoveryCanceled = true;
        Util.ba.cancelDiscovery();

        socket = null;
        //we want the UI to update while the blocking call "socket.connect();" is made, so it's it in a thread
        if(!connecting) {
            new Thread(connectRunnable).start();
        } else{
            Toast.makeText(this, "device is aready connecting", Toast.LENGTH_SHORT).show();
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(!devices.contains(device)) {
                    // add the device to the list
                    devices.add(device);
                    deviceNames.add(device.getName());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(c, "host found: " + deviceNames.get(deviceNames.size() - 1), Toast.LENGTH_SHORT).show();
                }
            } else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                // bluetooth discovery started
                statusText.setText("discovering devices...");
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // bluetooth discovery finished or was canceled
                if(!discoveryCanceled) {
                    statusText.setText("discovery finished. " + devices.size() + " devices found.");
                    progressBar.setVisibility(View.GONE);
                }
            } else if("connectionFinished".equals(action)){
                // connected to the host successfully
                statusText.setText("connected to " + device.getName());
                progressBar.setVisibility(View.GONE);
                manageConnection();
            }
        }
    };

    private void manageConnection(){
        // this method is called when you are connected to a host
        try {
            c.unregisterReceiver(mReceiver);
        } catch (Exception e) {
            //ignore, probably already unregistered
        }
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        }catch(Exception e){
            e.printStackTrace();
        }

        Toast.makeText(c, "connected to " + device.getName(), Toast.LENGTH_SHORT).show();

        listen();
    }

    public void listen(){
        // this method waits for the host to send the character '0', which means you should proceed to the ChooseActivity. The host also sends '1's to check if you're still connected
        new Thread(new Runnable() {
            @Override
            public void run() {
                // to be able to print toasts in this thread
                Looper.prepare();
                String receivedString;
                while(true){
                    if(inputStream != null){
                        receivedString = Util.receiveString(inputStream);
                        if(receivedString.indexOf(0) >= 0){
                            // if you receive a '0', proceed to the ChooseActivity
                            c.startActivity(startChooseActivity);
                            finish();
                            break;
                        }

                    } else{
                        // the inputStream is null, so the connection to the host is lost
                        disconnect();
                        Toast.makeText(Util.c, "An error occurred", Toast.LENGTH_SHORT).show();
                        // try to reconnect
                        connectToDevice(device);
                        break;
                    }
                }
            }
        }).start();
    }

    public static void disconnect(){
        // this method disconnects you from the host if connected
        try{
            inputStream.close();
            inputStream = null;

            outputStream.close();
            outputStream = null;

            socket.close();
            socket = null;

            device = null;
        } catch(Exception e){
            e.printStackTrace();
            //probably not yet connected
        }
    }
}