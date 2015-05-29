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
import android.widget.Filter;

import java.util.ArrayList;

import ch.imlee.maturarbeit.settings.TestActivity;

public class Host implements Runnable {

    Util util = new Util();
    Context c;
    ArrayList<BluetoothDevice> devices = new ArrayList<>();
    ArrayAdapter<BluetoothDevice> adapter;
    BluetoothServerSocket serverSocket;
    BluetoothServerSocket tempServerSocket;
    BluetoothSocket socket;
    Thread acceptThread = new Thread(this, "acceptThread");
    IntentFilter filter;


    public Host(Context context) {
        c = context;
        util.initBluetooth(c);
        Util.ba.setName(TestActivity.usernameEditText.getText().toString() + "_HOST");
        adapter = new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, devices);

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);

        c.startActivity(discoverableIntent);

        TestActivity.listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        try {
            tempServerSocket = Util.ba.listenUsingRfcommWithServiceRecord(TestActivity.usernameEditText.getText().toString(), Util.generateUUID());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        serverSocket = tempServerSocket;

        acceptConnections();


    }

    public void run() {

        try {
            socket = serverSocket.accept();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("...");
        System.out.println("connected");
        System.out.println("...");
        c.sendBroadcast(new Intent("finished"));
    }
    private final BroadcastReceiver threadFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            manageConnection();
        }
    };

    public void acceptConnections() {
        System.out.println("...");
        System.out.println("accepting connections...");
        System.out.println("...");
        Toast.makeText(c, "waiting for connections", Toast.LENGTH_SHORT).show();

        filter = new IntentFilter("finished");
        c.registerReceiver(this.threadFinishedReceiver, filter);

        acceptThread.start();
        
    }

    public void manageConnection() {
        System.out.println("...");
        System.out.println("managing connection...");
        System.out.println("...");
        c.unregisterReceiver(threadFinishedReceiver);

        try {
            Util.inputStream = socket.getInputStream();
            Util.outputStream = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            while (Util.inputStream.available() <= 0) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        Toast.makeText(c, Util.receiveString(), Toast.LENGTH_SHORT).show();


    }

    public void cancelAccept() {
        try {
            acceptThread.interrupt();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}






