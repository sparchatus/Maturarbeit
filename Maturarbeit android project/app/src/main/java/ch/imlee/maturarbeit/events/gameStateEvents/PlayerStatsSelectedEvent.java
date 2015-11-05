package ch.imlee.maturarbeit.events.gameStateEvents;

import android.os.Looper;
import android.os.Handler;

import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.game.entity.PlayerType;
import ch.imlee.maturarbeit.activities.ChooseActivity;

/**
 * Created by Lukas on 20.06.2015.
 */
public class PlayerStatsSelectedEvent extends GameStateEvent {
    private final PlayerType TYPE;
    private final byte TEAM;

    public PlayerStatsSelectedEvent(PlayerType type, byte team){
        TYPE = type;
        TEAM = team;
        serverEvent = true;
    }

    public PlayerStatsSelectedEvent(String eventString){
        TYPE = PlayerType.values()[Integer.parseInt(eventString.substring(2,eventString.indexOf(',')))];
        TEAM = Byte.parseByte(eventString.substring(eventString.indexOf(',')+1, eventString.indexOf("i")));
    }

    @Override
    public String toString(){
        return super.toString() + 's' + TYPE.ordinal() + ',' + TEAM + 'i' + senderID;
    }

    @Override
    public boolean handle(byte id){
        ChooseActivity.gameStartEvent.setPlayer(TYPE,
                TEAM,
                id);
        ++ChooseActivity.playersReady;
        if(ChooseActivity.playersReady == Host.sockets.size()){
            ChooseActivity.startGameButton.setClickable(true);
            // because we can only change the button's text in the UI thread, we have to do it with a Handler
            Handler setText = new Handler(Looper.getMainLooper());
            setText.post(new Runnable() {
                public void run() {
                    ChooseActivity.startGameButton.setText("Start Game");
                }
            });
        }
        return true;
    }
}