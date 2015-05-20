package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.app.Activity;

import java.util.ArrayList;
import java.util.UUID;

import ch.imlee.maturarbeit.settings.TestActivity;

public class Host implements Runnable{

    Util util = new Util();
    Context c;
    ArrayList<BluetoothDevice> devices = new ArrayList<>();
    ArrayAdapter<BluetoothDevice> adapter;
    BluetoothServerSocket serverSocket;
    BluetoothSocket socket;
    Thread acceptThread = new Thread(this, "acceptThread");


    public Host(Context context){
        c = context;
        util.initBluetooth(c);
        Util.ba.setName(TestActivity.usernameEditText.getText().toString() + "_HOST");
        adapter = new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, devices);
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        c.startActivity(discoverableIntent);
        try{
            Thread.sleep(3000);
        } catch(Exception e){
            e.printStackTrace();
        }
        if(Util.ba.getState() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Toast.makeText(c, "Device must be discoverable", Toast.LENGTH_SHORT).show();
            try{
                Thread.sleep(1500);
            } catch(Exception e){
                e.printStackTrace();
            }

        }

        TestActivity.listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        try{
            serverSocket = Util.ba.listenUsingRfcommWithServiceRecord(TestActivity.usernameEditText.getText().toString(), UUID.fromString(Util.generateUUID()));
        } catch(Exception e){
            e.printStackTrace();
        }
        acceptThread.start();


    }
    public void run(){
        while(true){
            try {
                socket = serverSocket.accept();
            } catch (Exception e){
                break;
            }
            if(socket != null){
                cancelAccept(); //TODO: for multiple connections, change this
            }

        }
    }
    public void cancelAccept(){
        try{
            serverSocket.close();
        } catch (Exception e){

        }
    }
}






