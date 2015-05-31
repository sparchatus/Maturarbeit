package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Util{
    public static BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
    static Context c;

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