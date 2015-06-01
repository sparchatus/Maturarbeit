package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import ch.imlee.maturarbeit.main.StartActivity;

public class Util{
    public static BluetoothAdapter ba;
    static Context c;

    public static void initBluetooth(Context context) {
        c = context;
        ba = BluetoothAdapter.getDefaultAdapter();

        if (ba == null) {
            //TODO: Make it work

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
}