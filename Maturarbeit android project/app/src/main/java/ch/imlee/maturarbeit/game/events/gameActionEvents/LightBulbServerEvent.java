package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.entity.User;

/**
 * Created by Sandro on 12.08.2015.
 */
public class LightBulbServerEvent extends GameActionEvent {

    private final int LIGHT_BULB_ID;

    public LightBulbServerEvent(User user, int lightBulbId){
        senderID = user.getID();
        LIGHT_BULB_ID = lightBulbId;
    }
    public LightBulbServerEvent(String string){
        senderID = Byte.parseByte(Character.toString(string.charAt(2)));
        LIGHT_BULB_ID = Integer.parseInt(Character.toString(string.charAt(3)));
    }

    @Override
    public String toString() {
        return super.toString() + 'C' + senderID + LIGHT_BULB_ID;
    }

    @Override
    public void apply() {
        //if (GameThread.getLightBulbArray()[LIGHT_BULB_ID] == null){
            new LightBulbEvent(senderID, LIGHT_BULB_ID, true).send();
        //}
    }
}
