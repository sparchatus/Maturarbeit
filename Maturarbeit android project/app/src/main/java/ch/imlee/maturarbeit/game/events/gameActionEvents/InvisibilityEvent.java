package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 18.06.2015.
 */
public class InvisibilityEvent extends GameActionEvent{
    public final boolean INVISIBLE;

    public InvisibilityEvent(String eventString){
        senderID = Byte.valueOf(eventString.substring(2, 3));
        if (eventString.endsWith("1")) {
            INVISIBLE = true;
        }else {
            INVISIBLE = false;
        }
    }

    public InvisibilityEvent(byte playerId, boolean invisible){
        senderID = playerId;
        INVISIBLE = invisible;
    }

    @Override
    public String toString() {
        if (INVISIBLE) {
            return super.toString() + "I" + senderID + "1";
        }else{
            return super.toString() + "I" + senderID + "0";
        }
    }

    @Override
    public void apply() {
        GameThread.getPlayerArray()[senderID].setInvisible(INVISIBLE);
    }
}
