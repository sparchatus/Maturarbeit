package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ch.imlee.maturarbeit.activities.StartActivity;

public class Host extends StartActivity {

    private static Context c;

    public static ArrayList<String> deviceNames = new ArrayList<>();

    public static ArrayAdapter<String> adapter; // the ArrayAdapter links the deviceNames List with the ListView from the StartActivity

    private static BluetoothServerSocket serverSocket;
    public static ArrayList<BluetoothSocket> sockets = new ArrayList<>();
    public static ArrayList<InputStream> inputStreams = new ArrayList<>();
    public static ArrayList<OutputStream> outputStreams = new ArrayList<>();

    // the acceptRunnable accepts incoming connections as long as it's active
    private Runnable acceptRunnable = new Runnable() {
        @Override
        public void run() {
            int i = 0;
            while (i < 7) {
                // workaround for random Exceptions: repeat until tempServerSocket is not null anymore,
                // normally this should only do one loop
                serverSocket = null;
                do {
                    try {
                        // the serverSocket is used to listen for incoming connections
                        serverSocket = Util.ba.listenUsingRfcommWithServiceRecord
                                (StartActivity.usernameEditText.getText().toString(), Util.getUUID(i));
                        Log.i("acceptThread", "ServerSocket created: " + serverSocket.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (serverSocket == null);

                try {
                    // this method blocks until the Thread gets interrupted or a client connects
                    sockets.add(serverSocket.accept());
                    manageConnection(sockets.get(sockets.size() - 1));
                    ++i;
                } catch (Exception e) {
                    e.printStackTrace();
                    // "Operation Canceled" gets thrown when the Host presses the "Start Game" Button
                    // and the acceptThread gets interrupted
                    if (e.getMessage().equals("Operation Canceled")) {
                        Log.i("acceptThread", "acceptThread canceled");
                        return;
                    }
                }
            }
        }
    };

    Thread acceptThread;

    public Host(Context context) {
        c = context;
        Util.ba.setName(StartActivity.usernameEditText.getText().toString());

        // this is the Intent used to make the device discoverable for the Clients
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        c.startActivity(discoverableIntent);

        // the ArrayAdapter displays the elements of deviceNames as a ListView on the screen
        adapter = new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, deviceNames);
        StartActivity.listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Toast.makeText(c, "waiting for connections", Toast.LENGTH_SHORT).show();
        acceptThread = new Thread(acceptRunnable);
        acceptThread.start();
    }

    // it's synchronized so connections won't interfere
    private synchronized void manageConnection(BluetoothSocket socket) {
        // this method adds the socket and its attributes to the corresponding lists for easier access
        String deviceName = socket.getRemoteDevice().getName();
        if(deviceName != null) {
            deviceNames.add(deviceName);
            // because only the UI Thread can call adapter.notifyDataSetChanged(), we have to call it this way
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
            try {
                inputStreams.add(socket.getInputStream());
                outputStreams.add(socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            refreshConnectedDevices();
        }
    }

    public static void disconnect(){
        // this method disconnects the device from all clients
        while(!sockets.isEmpty()){
            removeDevice(0);
        }
        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshConnectedDevices(){
        // this method checks weather all devices are still connected. If not, they get removed
        for(int i = 0; i < sockets.size(); ++i){
            try{
                outputStreams.get(i).write(1);
            } catch (Exception e){
                e.printStackTrace();
                // connection lost, remove from list
                removeDevice(i);
                --i;
            }
            // check if the same device is in the list twice, if so, remove the duplicates
            String address = sockets.get(i).getRemoteDevice().getAddress();
            for(int j = i+1; j < sockets.size(); ++j){
                if(sockets.get(j).getRemoteDevice().getAddress().equals(address)) {
                    removeDevice(j);
                    --j;
                }
            }
        }
    }

    public void cancelAcceptReceiver(){
        // this method stops the acceptThread from accepting more connections
        try {
            acceptThread.interrupt();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void removeDevice(int i){
        // this method removes a specific client
        try {
            sockets.get(i).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sockets.remove(i);
        outputStreams.remove(i);
        inputStreams.remove(i);
        deviceNames.remove(i);
    }

}
