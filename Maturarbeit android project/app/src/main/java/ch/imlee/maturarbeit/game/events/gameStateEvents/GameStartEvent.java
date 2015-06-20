package ch.imlee.maturarbeit.game.events.gameStateEvents;

import android.content.Intent;

import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.MapEnum;
import ch.imlee.maturarbeit.game.entity.PlayerType;

/**
 * Created by Lukas on 17.06.2015.
 */
public class GameStartEvent extends GameStateEvent {


    @Override
    public void handle(){
        // start the game (finally)
        Util.c.startActivity(new Intent(Util.c, GameClient.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private final PlayerType[]TYPE;
    private final byte USER_ID;
    private final byte[]TEAM;
    private final MapEnum MAP;

    public GameStartEvent(String eventString){
        TYPE = new PlayerType[1];
        USER_ID = 0;
        TEAM = new byte[1];
        MAP = MapEnum.TEST_MAP_2;
    }

    public GameStartEvent(PlayerType[] type, byte[] team, byte userID, MapEnum map) {
        TYPE = type;
        USER_ID = userID;
        TEAM = team;
        MAP = map;
    }

    @Override
    public String toString() {
        String playerInfo = "";
        for (int i = 0; i < TYPE.length; i++){
            playerInfo += "p" + TYPE[i].ordinal() + "t" + TEAM[i];
        }
        return super.toString() + 'S' + playerInfo + "i" + USER_ID + "m" + MAP.ordinal();
    }


}