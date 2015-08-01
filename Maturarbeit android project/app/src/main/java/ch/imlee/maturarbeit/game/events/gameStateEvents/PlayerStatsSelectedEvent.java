package ch.imlee.maturarbeit.game.events.gameStateEvents;

import android.os.Looper;
import android.os.Handler;
import android.widget.Button;

import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.game.entity.PlayerType;
import ch.imlee.maturarbeit.main.ChooseActivity;

/**
 * Created by Lukas on 20.06.2015.
 */
public class PlayerStatsSelectedEvent extends GameStateEvent {
    private final PlayerType TYPE;
    private final byte TEAM;

    public PlayerStatsSelectedEvent(PlayerType type, byte team){
        TYPE = type;
        TEAM = team;
    }

    public PlayerStatsSelectedEvent(String string){
        TYPE = PlayerType.values()[Integer.parseInt(string.substring(2,string.indexOf(',')))];
        TEAM = Byte.parseByte(string.substring(string.indexOf(',')+1));
    }

    @Override
    public String toString(){
        return super.toString() + 's' + TYPE.ordinal() + ',' + TEAM;
    }

    @Override
    public boolean handle(byte id){
        ChooseActivity.gameStartEvent.setPlayer(TYPE, TEAM, id);
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