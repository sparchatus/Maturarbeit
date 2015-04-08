package ch.imlee.maturarbeit.settings;

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

public class TestActivity extends ActionBarActivity {
    private static boolean buttonPressed = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
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
        ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        ListView listView = (ListView) this.findViewById(R.id.listView);
        TextView modeDependantText = (TextView) this.findViewById(R.id.modeDependantText);

        Button hostButton = (Button) this.findViewById(R.id.hostButton);
        Button joinButton = (Button) this.findViewById(R.id.joinButton);
        TextView usernameTextView = (TextView) this.findViewById(R.id.usernameTextView);
        EditText usernameEditText = (EditText) this.findViewById(R.id.usernameEditText);

        buttonPressed = true;



        modeDependantText.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        hostButton.setVisibility(View.INVISIBLE);
        joinButton.setVisibility(View.INVISIBLE);
        usernameTextView.setVisibility(View.INVISIBLE);
        usernameEditText.setVisibility(View.INVISIBLE);

        if(view.getId()==R.id.hostButton){
            //host
            modeDependantText.setText("waiting for Players");

        }
        else {
            //client
            modeDependantText.setText("searching for hosts");
        }
    }

    public void onBackPressed(){
        ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        ListView listView = (ListView) this.findViewById(R.id.listView);
        TextView modeDependantText = (TextView) this.findViewById(R.id.modeDependantText);

        Button hostButton = (Button) this.findViewById(R.id.hostButton);
        Button joinButton = (Button) this.findViewById(R.id.joinButton);
        TextView usernameTextView = (TextView) this.findViewById(R.id.usernameTextView);
        EditText usernameEditText = (EditText) this.findViewById(R.id.usernameEditText);

        if(buttonPressed){
            hostButton.setVisibility(View.VISIBLE);
            joinButton.setVisibility(View.VISIBLE);
            usernameTextView.setVisibility(View.VISIBLE);
            usernameEditText.setVisibility(View.VISIBLE);

            modeDependantText.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
