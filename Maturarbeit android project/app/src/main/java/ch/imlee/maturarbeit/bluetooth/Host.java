package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ch.imlee.maturarbeit.main.StartActivity;

public class Host implements Runnable {

    Util util = new Util();
    Context c;

    ArrayList<BluetoothDevice> devices = new ArrayList<>();
    ArrayList<String> deviceNames = new ArrayList<>();

    ArrayAdapter<String> adapter;

    BluetoothServerSocket serverSocket;
    BluetoothServerSocket tempServerSocket;
    public ArrayList<BluetoothSocket> sockets = new ArrayList<>();
    ArrayList<InputStream> inputStreams = new ArrayList<>();
    ArrayList<OutputStream> outputStreams = new ArrayList<>();

    Thread acceptThread = new Thread(this, "acceptThread");

    IntentFilter filter;


    public Host(Context context) {
        c = context;
        // to make the host identifiable
        Util.ba.setName(StartActivity.usernameEditText.getText().toString() + "_HOST");

        // TODO: put this in a thread which often checks whether the device is still discoverable, no need to put it here
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);

        c.startActivity(discoverableIntent);
        // endTODO:

        adapter = new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, deviceNames);
        StartActivity.listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        try {
            tempServerSocket = Util.ba.listenUsingRfcommWithServiceRecord(StartActivity.usernameEditText.getText().toString(), Util.generateUUID());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        serverSocket = tempServerSocket;

        filter = new IntentFilter("finished");
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
        System.out.println("...");
        System.out.println("accepting connections...");
        System.out.println("...");

        Toast.makeText(c, "waiting for connections", Toast.LENGTH_SHORT).show();

        acceptThread.start();
    }

    public void run() {
        while(true) {
            try {
                sockets.add(serverSocket.accept());
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            System.out.println("...");
            System.out.println("connected");
            System.out.println("...");
            c.sendBroadcast(new Intent("finished"));
        }
    }

    // it's synchronized so connections won't interfere
    private synchronized void manageConnection() {

        System.out.println("...");
        System.out.println("managing connection with " + sockets.get(sockets.size() - 1).getRemoteDevice().getName());
        System.out.println("...");

        devices.add(sockets.get(sockets.size() - 1).getRemoteDevice());
        deviceNames.add(devices.get(devices.size() - 1).getName());
        adapter.notifyDataSetChanged();

        try {
            inputStreams.add(sockets.get(sockets.size() -1).getInputStream());
            outputStreams.add(sockets.get(sockets.size() -1).getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void cancelAccept() {
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






