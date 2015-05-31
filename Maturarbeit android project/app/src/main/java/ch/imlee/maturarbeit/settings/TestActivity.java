package ch.imlee.maturarbeit.settings;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.bluetooth.Client;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {

    Host host;

    public static ProgressBar progressBar;
    public static ListView listView;
    public static TextView statusText;

    public static Button hostButton;
    public static Button joinButton;
    public static Button startButton;
    public static TextView usernameTextView;
    public static EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        // for design reasons, don't allow landscape mode in the menu
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initialize();
    }
    private void initialize(){
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
        usernameEditText.setText(Util.ba.getName());

        // used in the onBackPressed() method
        //buttonPressed = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

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
            startButton.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            statusText.setText("waiting for Players");
            host = new Host(getApplicationContext());

        }
        else if(view.getId()==R.id.joinButton){
            // join game as client
            statusText.setText("searching for hosts");
            new Client(getApplicationContext());
        } else{
            // _HOST ending no longer needed, this keeps the BloetoothAdapter the name which the user entered
            if(host.sockets.size() == 0){
                Toast.makeText(getApplicationContext(), "at least one device must be connected", Toast.LENGTH_LONG).show();
            } else{
                host.cancelAccept();
                //TODO: start game
            }
        }
    }

    public void onBackPressed(){

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
}