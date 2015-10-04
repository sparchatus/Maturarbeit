package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.Map;

/**
 * Created by Sandro on 03.10.2015.
 */
public class LightBulbStandEvent extends GameActionEvent{

    public final byte POSSESSOR_ID;
    public final byte STAND_ID;


    public LightBulbStandEvent(String eventString){
        super((byte)0);
        POSSESSOR_ID = Byte.valueOf(eventString.substring(eventString.indexOf('p') + 1, eventString.indexOf('s')));;
        STAND_ID = Byte.valueOf(eventString.substring(eventString.indexOf('s') + 1, eventString.indexOf('i')));
    }

    public LightBulbStandEvent(LightBulbStandServerEvent lightBulbStandServerEvent){
        super((byte)0);
        POSSESSOR_ID = lightBulbStandServerEvent.getSenderID();
        STAND_ID = lightBulbStandServerEvent.STAND_ID;
    }

    @Override
    public String toString() {
        return super.toString() + 'E' + 'p' + POSSESSOR_ID + 's' + STAND_ID + 'i' + senderID;
    }

    @Override
    public void apply() {
        GameThread.getPlayerArray()[POSSESSOR_ID].putBulbOnStand(STAND_ID);
    }
}
