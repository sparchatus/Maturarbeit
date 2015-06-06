package ch.imlee.maturarbeit.bluetooth;

import ch.imlee.maturarbeit.main.ChooseActivity;
import ch.imlee.maturarbeit.main.StartActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;
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

public class Client implements Runnable{
    private static Context c;

    private static ArrayList<BluetoothDevice> devices;
    private static ArrayList<String> deviceNames;
    private static ArrayAdapter<String> adapter;

    private static BluetoothSocket socket;
    private static BluetoothDevice device;
    private static String deviceName;

    private static InputStream inputStream;
    private static OutputStream outputStream;

    private Thread connectThread = new Thread(this, "connectThread");

    // used to check whether device discovery finished automatically or was cancelled
    private static boolean discoveryCancelled = false;

    public Client(Context context){
        c = context;

        // name isn't allowed to end with "_HOST", because that's how a host is identified
        if(StartActivity.usernameEditText.getText().toString().endsWith("_HOST")){
                Toast.makeText(c, "Invalid Username", Toast.LENGTH_LONG).show();
                StartActivity.hostButton.setVisibility(View.VISIBLE);
                StartActivity.joinButton.setVisibility(View.VISIBLE);
                StartActivity.usernameTextView.setVisibility(View.VISIBLE);
                StartActivity.usernameEditText.setVisibility(View.VISIBLE);

                StartActivity.statusText.setVisibility(View.INVISIBLE);
                StartActivity.listView.setVisibility(View.INVISIBLE);
                StartActivity.progressBar.setVisibility(View.INVISIBLE);

        }
        Util.ba.setName(StartActivity.usernameEditText.getText().toString());
        findDevices();
    }

    private void findDevices() {
        devices = new ArrayList<>();
        deviceNames = new ArrayList<>();
        // the adapter puts the found devices into the ListView using the deviceNames ArrayList
        adapter = new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, deviceNames);
        StartActivity.listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        StartActivity.listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (Util.ba.isDiscovering()) {
                    discoveryCancelled = true;
                    Util.ba.cancelDiscovery();
                }
                StartActivity.progressBar.setVisibility(View.VISIBLE);
                StartActivity.statusText.setText("connecting to " + deviceNames.get(position));
                connectToDevice(devices.get(position));
            }
        });


        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction("connectionFinished");

        c.registerReceiver(this.mReceiver, filter); // Don't forget to unregister during onDestroy

        Util.ba.startDiscovery();
    }

    private void connectToDevice(BluetoothDevice btDevice){
        device = btDevice;
        discoveryCancelled = true;
        Util.ba.cancelDiscovery();
        StartActivity.statusText.setText("connecting...");

        socket = null;
        //we want the UI to update while the blocking call "socket.connect();" is made, so it's it in a thread
        connectThread.start();
    }

    public void run(){
        // to allow Toasts in this Thread
        Looper.prepare();
        try{
            socket = device.createRfcommSocketToServiceRecord(Util.generateUUID());

            // the generated UUID contains the version name and code, so only players with the same game version can play together.
            // Todo: doesn't work anymore, workaround needed.
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("...");
        System.out.println("attempting to connect...");
        System.out.println("...");

        // because the connection doesn't always work at the first try
        while(true)
        try {
            socket.connect();
            break;
        } catch (Exception e) {
            Toast.makeText(c, "connection failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            //try again
        }

        deviceName = socket.getRemoteDevice().getName().substring(0,socket.getRemoteDevice().getName().length()-5);

        System.out.println("...");
        System.out.println("connection successful");
        System.out.println("...");

        c.sendBroadcast(new Intent("connectionFinished"));
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
                StartActivity.statusText.setText("discovering devices...");
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if(!discoveryCancelled) {
                    StartActivity.statusText.setText("discovery finished. " + devices.size() + " devices found.");
                    StartActivity.progressBar.setVisibility(View.GONE);
                }
            } else if("connectionFinished".equals(action)){
                manageConnection();
            }
        }
    };

    private void manageConnection(){
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
            System.exit(1);
        }
        Util.sendString(outputStream, "hello " + deviceName + ", I'm " + Util.ba.getName());

        Toast.makeText(c, "connected to " + device.getName().substring(0,device.getName().length()-5), Toast.LENGTH_SHORT).show();

        listen();
    }

    public static void listen(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(Util.receiveString(inputStream).length() > 0){
                        StartActivity.startChooseActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity(StartActivity.startChooseActivity);
                        break;
                    }

                }
            }
        }).start();
    }

    public static void disconnect(){
        try{
            inputStream.close();
            inputStream = null;

            outputStream.close();
            outputStream = null;

            socket.close();
            socket = null;
        } catch(Exception e){
            e.printStackTrace();
            //probably not yet connected
        }
    }
}