package ch.imlee.maturarbeit.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;


/**
 * Created by Lukas on 09.04.2015.
 */
public class Util{
    public static BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
    Context c;
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
