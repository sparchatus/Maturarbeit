package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;
import ch.imlee.maturarbeit.events.Event;
import ch.imlee.maturarbeit.events.EventHandler;

public class Util{
    public static BluetoothAdapter ba;
    public static Context c;
    private static int version;
    private static UUID[] uuids = new UUID[7];

    public static void initBluetooth(Context context) {
        c = context;
        ba = BluetoothAdapter.getDefaultAdapter();
        try {
            version = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionCode;
            Log.i("version", "version number: " + version);
        } catch (PackageManager.NameNotFoundException e){
            Log.e("version", e.getMessage());
            System.exit(1);
        }
        uuids = generateUUIDs();

        // BluetoothAdapter.getDefaultAdapter() returns null if the device doesn't support bluetooth. If this is the case, an info message will be displayed and the app exits when "OK" is clicked
        if (ba == null) {
            new AlertDialog.Builder(c)
                    .setTitle("Error")
                    .setMessage("Your Device doesn't support bluetooth, which is needed for this game. Sorry about that.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            System.exit(1);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .create()
                    .show();

        } else {
            StartActivity.usernameEditText.setText(Util.ba.getName());
            // to close all current connections, restart the adapter
            // there is probably a better way to do this, but we've found it's the most consistent like this and it works on most devices this way
            ba.disable();
            while(ba.getState() != BluetoothAdapter.STATE_OFF){
                try{
                    Thread.sleep(10);
                }catch (Exception e){}
            }
            ba.enable();
        }
    }

    public static UUID[] generateUUIDs(){
        // these UUIDs are generated in a way which makes them unique to their version code, which only allows players with the same app version to play together
        UUID[] temp = new UUID[7];
        String standardUUID = "2053c9be-5702-11e5-885d-feff819cdc9f";
        for(int i = 0; i < 7; ++i){
            // the UUID formed contains the version number at the beginning, then a number from 0-6 for all the possible connections because bluetooth
            // supports a maximum of 7 connections and then it fills the String up with the standard UUID
            // the log10 casted to int stands for the number of characters taken up by the version code -1
            temp[i] = UUID.fromString(version + (i + standardUUID.substring((int)Math.log10(version)+2)));
            Log.i("UUID", "UUID created: " + temp[i]);
        }
        return temp;
        //return UUID.fromString("2053c9be-5702-11e5-885d-feff819cdc9a");
    }

    public static synchronized void sendString(OutputStream outputStream, String text){
        try {
            outputStream.write(text.getBytes());
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static String receiveString(InputStream inputStream){
        String text = "";
        try {
            // to receive a String, we have to receive every single byte and chain them together
            for(int i = inputStream.available(); i > 0; --i) {
                text += (char) inputStream.read();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return text;
    }

    public static void receiveEvents(InputStream inputStream, byte id){
        String string = "";
        char c;
        try {
            // if string.length != 0 but there is nothing available, wait for the event to be sent completely before returning
            while(inputStream.available() > 0 || string.length() != 0) {
                c = (char) inputStream.read();
                if(c == '|'){
                    // '|' is the terminating char of every Event
                    Log.v("events", "Event received: " + string);
                    Event event = Event.fromString(string);
                    if(StartActivity.deviceType == DeviceType.HOST){
                        event.send();
                    }
                    // handle / apply the received event
                    if(!event.handle(id)){
                        EventHandler.synchronizedAdd(event, id);
                    }


                    //new EventHandler(Event.fromString(string), id).start();
                    string = "";
                } else{
                    string += c;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            if(e instanceof NullPointerException){
                //TODO
                // if client: connection to host lost, stop game
                // if host: connection to one client lost, if it was the only client, stop game, else send GameLeftEvent and handle it
            }
        }
    }
    public static UUID getUUID(int i){
        return uuids[i];
    }

}




