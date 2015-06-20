package ch.imlee.maturarbeit.game.events.gameStateEvents;

import android.widget.Toast;

import ch.imlee.maturarbeit.bluetooth.Host;
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

    @Override
    public String toString(){
        return super.toString() + 't' + TYPE.ordinal() + ',' + TEAM;
    }

    @Override
    public boolean handle(){
        ChooseActivity.gameStartEvent.addPlayer(TYPE, TEAM);
        ++ChooseActivity.playersReady;
        if(ChooseActivity.playersReady == Host.sockets.size()){
            ChooseActivity.startGameButton.setClickable(true);
            ChooseActivity.startGameButton.setText("Start Game");
        }
        return true;
    }
}