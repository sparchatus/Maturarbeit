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
        INVISIBLE = (eventString.charAt(2) == '1');
    }

    public InvisibilityEvent(byte playerId, boolean invisible){
        super(playerId);
        INVISIBLE = invisible;
    }

    @Override
    public String toString() {
        char invisibleBit = '0';
        if(INVISIBLE) invisibleBit = '1';
        return super.toString() + 'I' + invisibleBit + 'i' + senderID;
    }

    @Override
    public void apply() {
        GameThread.getPlayerArray()[senderID].setInvisible(INVISIBLE);
    }
}
