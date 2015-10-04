package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.LightBulbStand;
import ch.imlee.maturarbeit.game.map.Map;

/**
 * Created by Sandro on 03.10.2015.
 */
public class LightBulbStandServerEvent extends GameActionEvent {

    public final byte STAND_ID;

    public LightBulbStandServerEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        serverEvent = true;
        STAND_ID = Byte.valueOf(eventString.substring(eventString.indexOf('s') + 1, eventString.indexOf('i')));
    }

    public LightBulbStandServerEvent(byte standID){
        super(GameThread.getUser().getID());
        serverEvent = true;
        STAND_ID = standID;
    }

    @Override
    public String toString() {
        return super.toString() + 'F' + 's' + STAND_ID + 'i' + senderID;
    }

    @Override
    public void apply() {
        if (Map.getFriendlyLightBulbStands(GameThread.getPlayerArray()[senderID].TEAM)[STAND_ID].isFree()){
            LightBulbStandEvent lightBulbStandEvent = new LightBulbStandEvent(this);
            lightBulbStandEvent.send();
            lightBulbStandEvent.apply();
        }
        for (LightBulbStand lightBulbStand:Map.getFriendlyLightBulbStands(GameThread.getPlayerArray()[senderID].TEAM)){
            if (lightBulbStand.isFree()){
                return;
            }
        }
        new GameWinEvent(GameThread.getPlayerArray()[senderID].TEAM).send();
        new GameWinEvent(GameThread.getPlayerArray()[senderID].TEAM).apply();
    }
}
