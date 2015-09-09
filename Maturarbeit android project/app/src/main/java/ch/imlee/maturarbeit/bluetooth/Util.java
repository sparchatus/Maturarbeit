package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import ch.imlee.maturarbeit.events.Event;
import ch.imlee.maturarbeit.events.EventHandler;

public class Util{
    public static BluetoothAdapter ba;
    public static Context c;

    public static void initBluetooth(Context context) {
        c = context;
        ba = BluetoothAdapter.getDefaultAdapter();

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

        }
        else {
            //to close all current connections, restart the adapter
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
        //TODO: somehow make the UUID unique to the version number
        /*
        try {
            return c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionCode + ' ' +
                    c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionName + ' ' + c.getPackageName();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
        */
        return UUID.fromString("2053c9be-5702-11e5-885d-feff819cdc9f");
        //return UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }

    public static synchronized void sendString(OutputStream outputStream, String text){
        try {
            outputStream.write(text.getBytes());
            //outputStream.flush();
            Log.v("bluetooth", "sent String: " + text);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static String receiveString(InputStream inputStream){
        String text = "";
        try {
            for(int i = inputStream.available(); i > 0; --i) {
                text = text + (char)inputStream.read();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return text;
    }

    // if an EventString was not yet completely received by the time this method returns, store the unfinished String here
    public static void receiveEvents(InputStream inputStream, byte id){
        String string = "";
        char c;
        try {
            // if string.length != 0 but there is nothing available, wait for the event to be sent completely before returning
            while(inputStream.available()>0 || string.length() != 0) {
                c = (char) inputStream.read();
                if(c == '|'){
                    Log.v("events", "Event received: " + string);
                    new EventHandler(Event.fromString(string), id).start();
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




