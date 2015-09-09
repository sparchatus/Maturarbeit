package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ch.imlee.maturarbeit.activities.StartActivity;

public class Host extends StartActivity implements Runnable {

    private static Context c;

    public static ArrayList<String> deviceNames = new ArrayList<>();

    public static ArrayAdapter<String> adapter;

    private static BluetoothServerSocket serverSocket;
    public static ArrayList<BluetoothSocket> sockets = new ArrayList<>();
    public static ArrayList<InputStream> inputStreams = new ArrayList<>();
    public static ArrayList<OutputStream> outputStreams = new ArrayList<>();

    private Thread acceptThread = new Thread(this, "acceptThread");



    public Host(Context context) {
        c = context;
        Util.ba.setName(StartActivity.usernameEditText.getText().toString());


        // TODO: put this in a thread which often checks whether the device is still discoverable, no need to put it here
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);

        c.startActivity(discoverableIntent);
        // endTODO:


        adapter = new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, deviceNames);
        StartActivity.listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // workaround for random Exceptions: repeat until tempServerSocket is not null anymore, normally this should only do one loop
        while(serverSocket == null) {
            try {
                serverSocket = Util.ba.listenUsingRfcommWithServiceRecord(StartActivity.usernameEditText.getText().toString(), Util.generateUUID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        IntentFilter filter = new IntentFilter("finished");
        c.registerReceiver(this.threadFinishedReceiver, filter);

        acceptConnections();
    }

    private final BroadcastReceiver threadFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            manageConnection();
        }
    };

    private void acceptConnections() {
        c.registerReceiver(this.cancelAcceptReceiver, new IntentFilter("cancelAccept"));

        Toast.makeText(c, "waiting for connections", Toast.LENGTH_SHORT).show();

        acceptThread.start();
    }

    public void run() {
        while(true) {
            try {
                sockets.add(serverSocket.accept());
                c.sendBroadcast(new Intent("finished"));
            } catch (Exception e) {
                e.printStackTrace();
                // "Operation Canceled" gets thrown when the Host presses the "Start Game" Button
                if(e.getMessage().equals("Operation Canceled")) {
                    Log.i("acceptThread", "acceptThread canceled");
                    break;
                }
            }
        }

    }

    // it's synchronized so connections won't interfere
    private synchronized void manageConnection() {
        deviceNames.add(sockets.get(sockets.size() - 1).getRemoteDevice().getName());
        adapter.notifyDataSetChanged();

        try {
            inputStreams.add(sockets.get(sockets.size() -1).getInputStream());
            outputStreams.add(sockets.get(sockets.size() -1).getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        refreshConnectedDevices();
    }

    public BroadcastReceiver cancelAcceptReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("cancelAccept")) {
                c.unregisterReceiver(cancelAcceptReceiver);
                try {
                    acceptThread.interrupt();
                    serverSocket.close();
                    c.unregisterReceiver(threadFinishedReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    };

    public static void disconnect(){
        for(int i = 0; i < sockets.size(); ++i){
            removeDevice(i);
        }
    }

    private void refreshConnectedDevices(){
        for(int i = 0; i < sockets.size(); ++i){
            try{
                outputStreams.get(i).write(1);
            } catch (Exception e){
                e.printStackTrace();
                // connection lost, remove from list
                removeDevice(i);
                --i;
            }
            // check if the same mac address is there multiple times, if so, remove the duplicates
            String address = sockets.get(i).getRemoteDevice().getAddress();
            for(int j = i+1; j < sockets.size(); ++j){
                if(sockets.get(j).getRemoteDevice().getAddress().equals(address)){
                    removeDevice(j);
                    --j;
                }
            }
        }
    }

    private static void removeDevice(int i){
        sockets.remove(i);
        outputStreams.remove(i);
        inputStreams.remove(i);
        deviceNames.remove(i);
    }

}
