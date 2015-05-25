package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageInfo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


/**
 * Created by Lukas on 09.04.2015.
 */
public class Util{
    public static BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
    static Context c;
    public static InputStream inputStream;
    public static OutputStream outputStream;
    public void initBluetooth(Context context) {
    c = context;


        if (ba == null) {
            //TODO: Make it work
        }
            /*
            new AlertDialog.Builder(new Activity())
                    .setTitle("Error")
                    .setMessage("Your Device seems to not have Bluetooth")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(1);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }*/
        else {
            enableBluetooth();
        }
    }

    public static UUID generateUUID(){
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

    public void enableBluetooth() {

        if (!ba.isEnabled()) {
            ba.enable();

        }
    }




    public static void sendString(String text){
        try {
            outputStream.write(text.getBytes());
            outputStream.flush();
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }


    }
    public static String receiveString(){
        String text = "";
        try {
            for(int i = inputStream.available(); i > 0; --i) {
                text = text + inputStream.read();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return text;
    }

}
