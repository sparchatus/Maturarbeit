package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

/**
 * Created by Sandro on 18.06.2015.
 */
public class InvisibilityEvent extends GameActionEvent{
    public final boolean INVISIBLE;

    public InvisibilityEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        senderID = Byte.valueOf(eventString.substring(2, 3));
        if (eventString.endsWith("1")) {
            INVISIBLE = true;
        }else {
            INVISIBLE = false;
        }
    }

    public InvisibilityEvent(byte playerId, boolean invisible){
        super(playerId);
        INVISIBLE = invisible;
    }

    @Override
    public String toString() {
        if (INVISIBLE) {
            return super.toString() + "I" + "1" + 'i' + senderID;
        }else{
            return super.toString() + "I" + "0" + 'i' + senderID;
        }
    }

    @Override
    public void apply() {
        GameThread.getPlayerArray()[senderID].setInvisible(INVISIBLE);
    }
}
