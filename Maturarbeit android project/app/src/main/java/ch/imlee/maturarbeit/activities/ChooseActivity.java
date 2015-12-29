package ch.imlee.maturarbeit.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.events.EventReceiver;
import ch.imlee.maturarbeit.events.gameStateEvents.GameStartEvent;
import ch.imlee.maturarbeit.events.gameStateEvents.PlayerStatsSelectedEvent;
import ch.imlee.maturarbeit.game.WaitUntilLoadedThread;
import ch.imlee.maturarbeit.game.entity.PlayerType;

public class ChooseActivity extends Activity implements View.OnClickListener{
    public static ImageView playerTypeImage;
    public static RadioGroup playerTypeGroup;
    public static TextView playerTypeDescription;
    public static int playersReady = 0;
    public static Button startGameButton;
    public static RelativeLayout relativeLayout;
    public static PlayerTypeInfo[] playerTypeInfos;

    private byte selectedTeam = -1;
    public int selectedPlayerType = -1;

    public static GameStartEvent gameStartEvent = new GameStartEvent();

    public static EventReceiver eventReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // don't allow landscape mode in the menu for design reasons
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_choose);

        playerTypeImage = (ImageView) findViewById(R.id.playerTypeImage);
        playerTypeImage.setAdjustViewBounds(true);
        startGameButton = (Button) findViewById(R.id.startGameButton);
        playerTypeDescription = (TextView) findViewById(R.id.playerTypeDescription);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        if(StartActivity.deviceType == DeviceType.HOST){
            startGameButton.setText("Clients Ready: " + playersReady + '/' + Host.sockets.size());
            startGameButton.setClickable(false);
        } else{
            startGameButton.setText("Ready?");
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        PlayerTypeInfo[] temp = {
                new PlayerTypeInfo("Ghost", "The Ghost can become invisible for a while", Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ghost))),
                new PlayerTypeInfo("Slime", "The Slime can leave a slime trail behind which makes himself faster, but the others slower", Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.slime))),
                new PlayerTypeInfo("Fluffy", "The Fluffy charges itself electrostatically by rolling around, allowing it to stun its opponents", Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.fluffy)))
        };
        playerTypeInfos = temp.clone();
        createPlayerTypeRadioButtons();

        eventReceiver = new EventReceiver();
        eventReceiver.start();
    }

    private void createPlayerTypeRadioButtons(){
        playerTypeGroup = (RadioGroup) findViewById(R.id.playerTypeGroup);
        RadioButton playerTypeRadioButton;
        for(int i = 0; i < playerTypeInfos.length; ++i){
            playerTypeRadioButton = new RadioButton(this);
            playerTypeRadioButton.setText(playerTypeInfos[i].getName());
            // to identify the button later
            playerTypeRadioButton.setId(i);
            playerTypeGroup.addView(playerTypeRadioButton);
            playerTypeRadioButton.setOnClickListener(this);
        }
    }

    public void onClick(View v){
        v.setSelected(true);
        // check whether clicked button is in the teamGroup or playerTypeGroup
        if(((RadioButton)v).getText().toString().startsWith("Team ")){
            // to get 0 for 'A' and 1 for 'B', we have to take their ASCII values by casting them to byte and subtract 65
            selectedTeam = (byte)(((RadioButton) v).getText().charAt(5)-65);
        }
        else{
            selectedPlayerType = (byte)v.getId();
            // display an image and a description for the selected PlayerType
            // display the image only if there is enough space on the screen
            // the space for the image is calculated by subtracting the bottom of the lowest playerTypeRadioButton by the top of the startButton

            playerTypeInfos[v.getId()].setActive();



        }
    }

    public void onStartGameClick(View v){
        if(checkInputs()){
            if(StartActivity.deviceType == DeviceType.HOST) {
                // we don't have to check whether everyone has selected a team and player type, because it's not clickable until then.
                gameStartEvent.setPlayer(PlayerType.values()[selectedPlayerType], selectedTeam, (byte)0, Util.ba.getName());
                gameStartEvent.send();
                gameStartEvent.setUserID((byte)0);
                gameStartEvent.handle((byte)0);
                new WaitUntilLoadedThread().start();
            } else{
                new PlayerStatsSelectedEvent(PlayerType.values()[selectedPlayerType], selectedTeam).send();
                startGameButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    public boolean checkInputs(){
        if(!(selectedPlayerType >=0 && selectedTeam >= 0)){
            Toast.makeText(this, "Please choose a team and a player type", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    //TODO: onBackPressed: send cancelling Event. If you were the host, cancel the game, otherwise show a Toast that a player left. If only the host remains, cancel the game
    //TODO: if only two players, automatically assign teams
}