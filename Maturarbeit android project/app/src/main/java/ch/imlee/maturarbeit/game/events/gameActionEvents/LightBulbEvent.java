package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.User;

/**
 * Created by Sandro on 12.08.2015.
 */
public class LightBulbEvent extends GameActionEvent{

    private final int LIGHT_BULB_ID;
    private final boolean PICKED_UP;

    public LightBulbEvent(byte playerId, int lightBulbId, boolean pickedUp){
        senderID = playerId;
        LIGHT_BULB_ID = lightBulbId;
        PICKED_UP = pickedUp;
    }
    public LightBulbEvent(String string){
        senderID = Byte.parseByte(Character.toString(string.charAt(2)));
        LIGHT_BULB_ID = Integer.parseInt(Character.toString(string.charAt(3)));;
        if (string.endsWith("i")){
            PICKED_UP = true;
        } else {
            PICKED_UP = false;
        }
    }

    @Override
    public String toString() {
        if (PICKED_UP) {
            return super.toString() + 'B' + senderID + LIGHT_BULB_ID + "1";
        }else{
            return super.toString()+ 'B' + senderID + LIGHT_BULB_ID + "0";
        }
    }

    @Override
    public void apply() {
        if (PICKED_UP) {
            GameThread.getPlayerArray()[senderID].flagReceived();
        }else {
            GameThread.getPlayerArray()[senderID].flagLost();
        }
    }
}
