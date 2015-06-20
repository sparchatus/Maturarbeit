package ch.imlee.maturarbeit.game.events.gameStateEvents;

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

    public PlayerStatsSelectedEvent(String string){
        if(!string.startsWith(super.toString()+'s')){
            try{
                throw new Exception("PlayerStatsSelectedEvent must start with \"Gs\"");
            } catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
        string = string.substring(2);
        TYPE = PlayerType.values()[Integer.parseInt(string.substring(0,string.indexOf(',')))];
        TEAM = Byte.parseByte(string.substring(string.indexOf(',')+1));
    }

    @Override
    public String toString(){
        return super.toString() + 's' + TYPE.ordinal() + ',' + TEAM;
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