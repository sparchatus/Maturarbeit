package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.User;

// this Event gets sent by clients when they want to take a LightBulb
public class LightBulbServerEvent extends GameActionEvent {

    private final byte LIGHT_BULB_ID;

    public LightBulbServerEvent(User user, byte lightBulbId){
        super(user.getID());
        LIGHT_BULB_ID = lightBulbId;
        serverEvent = true;
    }
    public LightBulbServerEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        LIGHT_BULB_ID = Byte.valueOf(eventString.substring(eventString.indexOf('1') + 1, eventString.indexOf('i')));
        serverEvent = true;
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
