package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.User;

/**
 * Created by Sandro on 12.08.2015.
 */
public class LightBulbServerEvent extends GameActionEvent {

    private final int PLAYER_ID;
    private final int LIGHT_BULB_ID;

    public LightBulbServerEvent(User user, int lightBulbId){
        PLAYER_ID = user.getID();
        LIGHT_BULB_ID = lightBulbId;
    }
    public LightBulbServerEvent(String string){
        PLAYER_ID = string.charAt(2);
        LIGHT_BULB_ID = string.charAt(3);
    }

    @Override
    public String toString() {
        return super.toString() + 'C' + PLAYER_ID;
    }

    @Override
    public void apply() {
        if (GameThread.getLightBulbArray()[LIGHT_BULB_ID] == null){
            new LightBulbEvent(PLAYER_ID, LIGHT_BULB_ID, true).send();
        }
    }
}
