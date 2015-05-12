package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;


/**
 * Created by Lukas on 09.04.2015.
 */
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
        else {
            enableBluetooth();
        }
    }

    public static String generateUUID(){
        try {
            return c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionCode + ' ' +
                    c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionName + ' ' + c.getPackageName();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void enableBluetooth() {

        if (!ba.isEnabled()) {
            ba.enable();

        }
    }


    // TODO when game starts, disable discovery
    public void cancelDiscovery(){
        ba.cancelDiscovery();
    }

}
