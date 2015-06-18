package ch.imlee.maturarbeit.game.events;

import ch.imlee.maturarbeit.game.MapEnum;
import ch.imlee.maturarbeit.game.entity.PlayerType;

/**
 * Created by Sandro on 17.06.2015.
 */
public class StartEvent extends Event{

    private final PlayerType[]TYPE;
    private final byte USER_ID;
    private final byte[]TEAM;
    private final MapEnum MAP;

    public StartEvent(String eventString){
        TYPE = new PlayerType[1];
        USER_ID = 0;
        TEAM = new byte[1];
        MAP = MapEnum.TEST_MAP_2;
    }

    public StartEvent(PlayerType[] type, byte[] team, byte userID, MapEnum map) {
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
        return "L" + playerInfo + "i" + USER_ID + "m" + MAP.ordinal();
    }
}
