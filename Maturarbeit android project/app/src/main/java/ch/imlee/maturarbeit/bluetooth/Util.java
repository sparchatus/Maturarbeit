package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Util{
    public static BluetoothAdapter ba;
    public static boolean gameStarted = false;

    public static void initBluetooth(Context context) {
        ba = BluetoothAdapter.getDefaultAdapter();

        if (ba == null) {
            //TODO: Make it work

            new AlertDialog.Builder(context)
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
        else if (!ba.isEnabled()) {
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
        return UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }

    public static void sendString(OutputStream outputStream, String text){
        try {
            outputStream.write(text.getBytes());
            outputStream.flush();
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    // TODO: the following method is used later to send Events
    /*
    public static void sendEvent(OutputStream outputStream, Event event){
        String text = event.toString();
        text = text + '|';
        try {
            outputStream.write(text.getBytes());
            outputStream.flush();
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    */

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

    // TODO: the following method is used later to receive an Event
    // (method must be called multiple times for multiple Events, should be done in a thread,
    // because InputStream.read() is a blocking call and returns only when a byte is available or an Exception is thrown.
    /*
    public static Event receiveNextEvent(InputStream inputStream){
        String text = "";
        char c;
        try {
            while(true) {
                c = (char) inputStream.read();
                if(c == '|'){
                    break;
                }
                else{
                    text = text + c;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Event(text);
    }
    */
}



















