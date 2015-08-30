package ch.imlee.maturarbeit.activities;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.bluetooth.Client;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;

public class StartActivity extends AppCompatActivity {

    Host host;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // don't allow landscape mode in the menu for design reasons
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initialize();
    }
    private void initialize(){
        //TODO: DEBUG
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.startLayout);
        Button chooseButton = new Button(this);
        /*
        chooseButton.setText("choose");
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startChooseActivity);
            }
        });
        layout.addView(chooseButton, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    */
        /*Button gameButton = new Button(this);
        gameButton.setText("game");
        final Intent startGameActivity = new Intent(getBaseContext(), GameClient.class);
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startGameActivity);
            }
        });
        layout.addView(gameButton, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        */
        //TODO: end debug

        registerReceiver(finishReceiver, new IntentFilter("finish"));

        startChooseActivity = new Intent(getBaseContext(), ChooseActivity.class);
        Util.initBluetooth(StartActivity.this);
        // making objects of the Views from activity_test.xml to manipulate them
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        listView = (ListView) this.findViewById(R.id.listView);
        listView.setBackgroundColor(Color.DKGRAY);


        statusText = (TextView) this.findViewById(R.id.statusText); // statusText is a TextView that displays the current status during the connection phase

        hostButton = (Button) this.findViewById(R.id.hostButton);
        joinButton = (Button) this.findViewById(R.id.joinButton);
        startButton = (Button) this.findViewById(R.id.startButton);
        usernameTextView = (TextView) this.findViewById(R.id.usernameTextView);
        usernameEditText = (EditText) this.findViewById(R.id.usernameEditText);
        // this is needed because the AlertDialog from the Util.initBluetooth() method is shown in a new thread, so it's possible that Util.ba is null
        try {
            usernameEditText.setText(Util.ba.getName());
        } catch(Exception e){
            e.printStackTrace();
        }

        // used in the onBackPressed() method
        //buttonPressed = true;
    }
    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_test, menu);
            return true;
        }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
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

        if(view.getId()==R.id.hostButton){
            // host game
            deviceType = DeviceType.HOST;
            startButton.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            statusText.setText("waiting for Players");
            host = new Host(getApplicationContext());

        }
        else if(view.getId()==R.id.joinButton){
            // join game as client
            deviceType = DeviceType.CLIENT;
            statusText.setText("searching for hosts");
            new Client(getApplicationContext());
        } else{
            // start the ChooseActivity
            if(Host.sockets.size() == 0){
                Toast.makeText(getApplicationContext(), "at least one device must be connected", Toast.LENGTH_LONG).show();
            } else{
                // notify others that they should start the ChooseActivity, this is done by simply sending the int 0
                for(int i = 0; i < Host.outputStreams.size(); ++i){
                    try {
                        Host.outputStreams.get(i).write(0);
                    } catch(Exception e){
                        e.printStackTrace();
                        if(e instanceof IOException){
                            Toast.makeText(Util.c, "lost connection to " + Host.sockets.get(i).getRemoteDevice().getName(), Toast.LENGTH_SHORT).show();
                            Host.sockets.remove(i);
                            Host.inputStreams.remove(i);
                            Host.outputStreams.remove(i);
                            Host.deviceNames.remove(i);
                            Host.adapter.notifyDataSetChanged();
                            --i;
                        }
                    }
                }
                // _HOST ending no longer needed, this gives the BluetoothAdapter the name which the user entered
                Util.ba.setName(usernameEditText.getText().toString());
                sendBroadcast(new Intent("cancelAccept"));
                // starts the ChooseActivity
                if(Host.sockets.size()>0) {
                    startActivity(startChooseActivity);
                    finish();
                }
            }
        }
    }

    public void onBackPressed(){
        //if connected to a host, disconnect
        Client.disconnect();
        Host.disconnect();


        //if(buttonPressed){
        if(Util.ba.isDiscovering()) {
            Util.ba.cancelDiscovery();
        }

        hostButton.setVisibility(View.VISIBLE);
        joinButton.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.GONE);
        usernameTextView.setVisibility(View.VISIBLE);
        usernameEditText.setVisibility(View.VISIBLE);

        statusText.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        //}
        //buttonPressed = false;
    }

    // BroadcastReceiver to finish this Activity as soon as ChooseActivity launches, this is only needed if the Client starts the new Activity
    BroadcastReceiver finishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("finish")){
                System.out.println("...");
                System.out.println("finishing");
                System.out.println("...");
                finish();
            }
        }
    };

    @Override
    public void onStop(){
        super.onStop();
        try {
            unregisterReceiver(finishReceiver);
        } catch(Exception e){
            // ignore, probably already unregistered
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // remove the "_HOST" ending of the BluetoothAdapter
        if(Util.ba.getName().endsWith("_HOST")){
            Util.ba.setName(Util.ba.getName().substring(0,Util.ba.getName().length()-5));
        }
    }
}