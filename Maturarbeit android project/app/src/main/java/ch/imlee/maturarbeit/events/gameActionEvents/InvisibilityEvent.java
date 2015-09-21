package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.activities.GameClient;
import ch.imlee.maturarbeit.events.gameStateEvents.GameCancelledEvent;
import ch.imlee.maturarbeit.game.GameThread;

/**
 * Created by Sandro on 18.06.2015.
 */
public class InvisibilityEvent extends GameActionEvent{
    public final boolean INVISIBLE;

    public InvisibilityEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        if (eventString.substring(eventString.indexOf('I'), eventString.indexOf('i')).equals('1')) {
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
        GameClient.getGameThread().getPlayerArray()[senderID].setInvisible(INVISIBLE);
    }
}
