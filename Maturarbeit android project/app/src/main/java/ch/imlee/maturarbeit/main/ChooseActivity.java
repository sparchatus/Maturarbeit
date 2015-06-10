package ch.imlee.maturarbeit.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ch.imlee.maturarbeit.R;

public class ChooseActivity extends ActionBarActivity implements View.OnClickListener{
    ImageView fluffImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        fluffImage = (ImageView) findViewById(R.id.fluffImage);
        createFluffRadioButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   /*
   private void createFluffRadioButtons(Fluff[] fluffs){

    }
    */

    // for testing, lets just use some examples:
    private void createFluffRadioButtons(){
        RadioGroup fluffGroup = (RadioGroup) findViewById(R.id.fluffGroup);
        RadioButton fluffRadioButton;
        for(int i = 0; i < 3; ++i){
            fluffRadioButton = new RadioButton(this);
            fluffRadioButton.setText("TestFluff Number " + (i+1));
            fluffGroup.addView(fluffRadioButton);
            fluffRadioButton.setOnClickListener(this);
        }
    }

    public void onClick(View v){
        v.setSelected(true);
        // check whether clicked button is in the teamGroup or fluffGroup
        if(!((RadioButton)v).getText().toString().startsWith("Team ")){
            fluffImage.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getBaseContext(), StartActivity.class));
    }


    //TODO: onBackPressed: send cancelling Event. If you were the host, cancel the game, otherwise show a Toast that a player left. If only the host remains, cancel the game












}
