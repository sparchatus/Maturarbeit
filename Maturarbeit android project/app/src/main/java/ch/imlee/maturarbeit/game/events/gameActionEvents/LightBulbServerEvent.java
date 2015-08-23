package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.User;

/**
 * Created by Sandro on 12.08.2015.
 */
public class LightBulbServerEvent extends GameActionEvent {

    private final byte LIGHT_BULB_ID;

    public LightBulbServerEvent(User user, byte lightBulbId){
        super(user.getID());
        LIGHT_BULB_ID = lightBulbId;
    }
    public LightBulbServerEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        LIGHT_BULB_ID = Byte.valueOf(eventString.substring(eventString.indexOf("l") + 1, eventString.indexOf("i")));
    }

    @Override
    public String toString() {
        return super.toString() + 'C' + 'l' + LIGHT_BULB_ID + 'i' + senderID;
    }

    @Override
    public void apply() {
        new LightBulbEvent(this).send();
        GameThread.getPlayerArray()[senderID].bulbReceived(LIGHT_BULB_ID);
    }

    public byte getLIGHT_BULB_ID(){
        return LIGHT_BULB_ID;
    }
}
