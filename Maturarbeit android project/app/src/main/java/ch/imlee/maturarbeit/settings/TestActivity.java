package ch.imlee.maturarbeit.settings;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.bluetooth.Client;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {
    private static boolean buttonPressed = false;




    public static ProgressBar progressBar;
    public static ListView listView;
    public static TextView modeDependantText;

    public static Button hostButton;
    public static Button joinButton;
    public static TextView usernameTextView;
    public static EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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




    public void onClick(View view) {
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        listView = (ListView) this.findViewById(R.id.listView);

        modeDependantText = (TextView) this.findViewById(R.id.modeDependantText);

        hostButton = (Button) this.findViewById(R.id.hostButton);
        joinButton = (Button) this.findViewById(R.id.joinButton);
        usernameTextView = (TextView) this.findViewById(R.id.usernameTextView);
        usernameEditText = (EditText) this.findViewById(R.id.usernameEditText);

        buttonPressed = true;



        modeDependantText.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);

        hostButton.setVisibility(View.INVISIBLE);
        joinButton.setVisibility(View.INVISIBLE);
        usernameTextView.setVisibility(View.INVISIBLE);
        usernameEditText.setVisibility(View.INVISIBLE);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameEditText.getWindowToken(), 0);

        if(view.getId()==R.id.hostButton){
            //host
            listView.setVisibility(View.VISIBLE);
            modeDependantText.setText("waiting for Players");
            new Host(getApplicationContext());

        }
        else {
            //client
            modeDependantText.setText("searching for hosts");
            new Client(getApplicationContext());
        }
    }


    public void onBackPressed(){

        if(buttonPressed){
            Util.ba.cancelDiscovery();

            hostButton.setVisibility(View.VISIBLE);
            joinButton.setVisibility(View.VISIBLE);
            usernameTextView.setVisibility(View.VISIBLE);
            usernameEditText.setVisibility(View.VISIBLE);

            modeDependantText.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
        buttonPressed = false;
    }

}
