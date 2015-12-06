package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

// this Event gets sent by clients when they want to take a LightBulb
public class LightBulbServerEvent extends GameActionEvent {

    public final byte LIGHT_BULB_ID;

    public LightBulbServerEvent(byte lightBulbId){
        super(GameThread.getUser().getID());
        LIGHT_BULB_ID = lightBulbId;
        serverEvent = true;
    }

    public LightBulbServerEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        LIGHT_BULB_ID = Byte.valueOf(eventString.substring(eventString.indexOf('l') + 1, eventString.indexOf('i')));
        serverEvent = true;
    }

    @Override
    public String toString() {
        return super.toString() + 'C' + 'l' + LIGHT_BULB_ID + 'i' + senderID;
    }

    @Override
    public void apply() {
        if(GameThread.getLightBulbArray()[LIGHT_BULB_ID].isPickable()) {
            new LightBulbEvent(senderID, LIGHT_BULB_ID, this).send();
            GameThread.getPlayerArray()[senderID].bulbReceived(LIGHT_BULB_ID);
        }
    }
}
