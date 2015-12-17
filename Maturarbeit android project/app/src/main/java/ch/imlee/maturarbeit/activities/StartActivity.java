package ch.imlee.maturarbeit.activities;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.bluetooth.Client;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

public class StartActivity extends Activity {

    public static DeviceType deviceType;

    // those Views should be accessible from outside this class
    public static ProgressBar progressBar;
    public static ListView listView;
    public static TextView statusText;

    public static Button hostButton;
    public static Button joinButton;
    public static Button startButton;
    public static TextView usernameTextView;
    public static EditText usernameEditText;
    public static Intent startChooseActivity;

    private Host host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // don't allow landscape mode in the menu for design reasons
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initialize();
    }

    private void initialize(){
        startChooseActivity = new Intent(getBaseContext(), ChooseActivity.class);
        startChooseActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // making objects of the Views from activity_start.xml to manipulate them
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar); // this is a rotating indicator. It shows when your device is connecting to a host or when it's searching for hosts.
        listView = (ListView) this.findViewById(R.id.listView); // the listView shows the available hosts or the connected clients, depending on your deviceType
        listView.setBackgroundColor(Color.DKGRAY);

        statusText = (TextView) this.findViewById(R.id.statusText); // statusText is a TextView that displays the current status during the connection phase

        hostButton = (Button) this.findViewById(R.id.hostButton);
        joinButton = (Button) this.findViewById(R.id.joinButton);
        startButton = (Button) this.findViewById(R.id.startButton); // this button is only visible to the host. It launches the ChooseActivity.
        usernameTextView = (TextView) this.findViewById(R.id.usernameTextView);
        usernameEditText = (EditText) this.findViewById(R.id.usernameEditText);

        Util.initBluetooth(this);
    }

    // this method is called when a button is clicked
    public void onClick(View view) {

        // adjust the views
        statusText.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);

        hostButton.setVisibility(View.INVISIBLE);
        joinButton.setVisibility(View.INVISIBLE);
        usernameTextView.setVisibility(View.INVISIBLE);
        usernameEditText.setVisibility(View.INVISIBLE);

        // hide the keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameEditText.getWindowToken(), 0);

        if(view.getId() == R.id.hostButton){
            // host game
            deviceType = DeviceType.HOST;
            startButton.setVisibility(View.VISIBLE);
            statusText.setText("waiting for Players");
            host = new Host(getApplicationContext());
        } else if(view.getId() == R.id.joinButton){
            // join game as client
            deviceType = DeviceType.CLIENT;
            statusText.setText("looking for Hosts");
            new Client(getApplicationContext());
        } else{
            // startButton got pressed, start the ChooseActivity if possible
            if(Host.sockets.size() == 0){
                Toast.makeText(getApplicationContext(), "at least one device must be connected", Toast.LENGTH_LONG).show();
            } else{
                // notify others that they should start the ChooseActivity, this is done by simply sending the byte 0
                for(int i = 0; i < Host.outputStreams.size(); ++i){
                    try {
                        Host.outputStreams.get(i).write(0);
                    } catch(Exception e){
                        e.printStackTrace();
                        if(e instanceof IOException){
                            // an IOException when writing a byte means that the connection to that device is lost, so it gets removed
                            Toast.makeText(Util.c, "lost connection to " + Host.sockets.get(i).getRemoteDevice().getName(), Toast.LENGTH_SHORT).show();
                            Host.sockets.remove(i);
                            Host.inputStreams.remove(i);
                            Host.outputStreams.remove(i);
                            Host.deviceNames.remove(i);
                            Host.adapter.notifyDataSetChanged();
                            // all elements in the outputStreams List got shifted to the left, so i needs to get decremented
                            --i;
                        }
                    }
                }
                //Util.ba.setName(usernameEditText.getText().toString());
                host.cancelAcceptReceiver();
                // starts the ChooseActivity
                // this has to be checked again
                if(Host.sockets.size() > 0) {
                    startActivity(startChooseActivity);
                    finish();
                }
            }
        }
    }

    @Override
    public void onBackPressed(){
        // this method automatically gets called when the back button is pressed
        //if connected, disconnect
        Client.disconnect();
        Host.disconnect();

        if(Util.ba.isDiscovering()) {
            Util.ba.cancelDiscovery();
        }

        // toggle the views back
        hostButton.setVisibility(View.VISIBLE);
        joinButton.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.GONE);
        usernameTextView.setVisibility(View.VISIBLE);
        usernameEditText.setVisibility(View.VISIBLE);

        statusText.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }
}