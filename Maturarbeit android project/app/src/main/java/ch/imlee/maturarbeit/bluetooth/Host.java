package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import ch.imlee.maturarbeit.settings.TestActivity;

public class Host implements Runnable{

    Util util = new Util();
    Context c;
    ArrayList<BluetoothDevice> devices = new ArrayList<>();
    ArrayAdapter<BluetoothDevice> adapter;
    BluetoothServerSocket serverSocket;
    BluetoothServerSocket tempServerSocket;
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


        TestActivity.listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        try{
            tempServerSocket = Util.ba.listenUsingRfcommWithServiceRecord(TestActivity.usernameEditText.getText().toString(), Util.generateUUID());
        } catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        serverSocket = tempServerSocket;
        acceptThread.start();


    }
    public void run(){
        Looper.prepare();
        Toast.makeText(c, "waiting for connections", Toast.LENGTH_SHORT).show();
        while(true){
            try {
                socket = serverSocket.accept();
            } catch (Exception e){
                e.printStackTrace();
                System.exit(1);
            }
            if(socket != null) {
                synchronized (Host.this) {


                    Toast.makeText(c, "connected to: " + socket.getRemoteDevice().getName(), Toast.LENGTH_SHORT).show();
                    //TestActivity.modeDependantText.setText("incoming connection: " + socket.getRemoteDevice().getName());
                    cancelAccept(); //TODO: for multiple connections, change this
                    while (socket.getRemoteDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                        Toast.makeText(c, "not yet connected", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(1500);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                    }
                    try {
                        Util.inputStream = socket.getInputStream();
                        Util.outputStream = socket.getOutputStream();
                    }catch(Exception e){
                        e.printStackTrace();
                        System.exit(1);
                    }
                    try {
                        while (Util.inputStream.available() <= 0) {
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                        System.exit(1);
                    }
                    Toast.makeText(c, Util.receiveString(), Toast.LENGTH_SHORT).show();

                    break;
                }
            }

        }
    }
    public void cancelAccept(){
        try{
            serverSocket.close();
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}






