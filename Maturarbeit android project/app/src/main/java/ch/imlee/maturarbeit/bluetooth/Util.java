package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

    public static void initBluetooth(Context context) {
        c = context;
        ba = BluetoothAdapter.getDefaultAdapter();

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

    public static UUID generateUUID(){
        // this is a randomly generated UUID, so only devices running this very app will be able to connect to each other
        // if the app is released, we'll update the UUID with each new version, assuring that only devices running the same version of the game can play with each other
        return UUID.fromString("2053c9be-5702-11e5-885d-feff819cdc9f");
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

}




