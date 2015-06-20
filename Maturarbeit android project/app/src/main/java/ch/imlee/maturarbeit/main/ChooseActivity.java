package ch.imlee.maturarbeit.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.MapEnum;
import ch.imlee.maturarbeit.game.entity.PlayerType;
import ch.imlee.maturarbeit.game.events.EventReceiver;
import ch.imlee.maturarbeit.game.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.game.events.gameStateEvents.PlayerStatsSelectedEvent;

public class ChooseActivity extends ActionBarActivity implements View.OnClickListener{
    ImageView fluffImage;
    public static int playersReady = 0;
    public static Button startGameButton;

    private byte selectedTeam;
    private int selectedPlayerType;

    public static GameStartEvent gameStartEvent;

    //TODO: DEBUG
    String[] fluffButtons = {"Ghost", "Slime", "Fluffy"};
    Thread eventReceiver = new EventReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // don't allow landscape mode in the menu for design reasons
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_choose);

        fluffImage = (ImageView) findViewById(R.id.fluffImage);
        startGameButton = (Button) findViewById(R.id.startGameButton);

        if(StartActivity.deviceType == DeviceType.HOST){
            startGameButton.setText("Clients Ready: " + playersReady + '/' + Host.sockets.size());
            startGameButton.setClickable(false);
        } else{
            startGameButton.setText("Ready?");
        }
        createFluffRadioButtons();

        gameStartEvent = new GameStartEvent(MapEnum.TEST_MAP_2);
        eventReceiver.start();
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

    private void createFluffRadioButtons(){
        RadioGroup fluffGroup = (RadioGroup) findViewById(R.id.fluffGroup);
        RadioButton fluffRadioButton;
        for(int i = 0; i < fluffButtons.length; ++i){
            fluffRadioButton = new RadioButton(this);
            fluffRadioButton.setText(fluffButtons[i]);
            // to identify the button later
            fluffRadioButton.setId(i);
            fluffGroup.addView(fluffRadioButton);
            fluffRadioButton.setOnClickListener(this);
        }
    }

    public void onClick(View v){
        v.setSelected(true);

        // check whether clicked button is in the teamGroup or fluffGroup
        if(((RadioButton)v).getText().toString().startsWith("Team ")){
            // to get 0 for 'A' and 1 for 'B', we have to do the following. This is because we cast a char to an int, so it takes the ASCII value, which is 65 for 'A' and 66 for 'B'
            selectedTeam = (byte)(((RadioButton) v).getText().charAt(5)-65);
        }
        else{
            selectedPlayerType = v.getId();

            fluffImage.setImageResource(R.mipmap.ic_launcher);
            fluffImage.setImageBitmap(BitmapFactory.decodeFile("src/main/res/drawable/fluffy.png"));
        }
    }

    public void onStartGameClick(View v){
        if(StartActivity.deviceType == DeviceType.HOST) {
            // check whether all Clients pressed "Ready?" Button:
            gameStartEvent.send();
            startActivity(new Intent(this, GameClient.class));
        } else{
            new PlayerStatsSelectedEvent(PlayerType.values()[selectedPlayerType], selectedTeam).send();
        }
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getBaseContext(), StartActivity.class));
    }


    //TODO: onBackPressed: send cancelling Event. If you were the host, cancel the game, otherwise show a Toast that a player left. If only the host remains, cancel the game
    //TODO: if only two players, automatically assign teams
    //TODO: actually start the game












}
