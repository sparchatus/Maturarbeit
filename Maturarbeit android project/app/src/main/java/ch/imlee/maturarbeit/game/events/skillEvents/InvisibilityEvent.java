package ch.imlee.maturarbeit.game.events.skillEvents;

import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 18.06.2015.
 */
public class InvisibilityEvent extends SkillEvent{
    public final byte SOURCE_PLAYER_ID;
    public final boolean INVISIBLE;

    public InvisibilityEvent(String eventString){
        SOURCE_PLAYER_ID = Byte.valueOf(eventString.substring(1, 2));
        if (Byte.valueOf(eventString.substring(2)) == 0) {
            INVISIBLE = false;
        }else {
            INVISIBLE = true;
        }
    }

    public InvisibilityEvent(byte sourcePlayerId, boolean invisible){
        SOURCE_PLAYER_ID = sourcePlayerId;
        INVISIBLE = invisible;
    }

    @Override
    public String toString() {
        if (INVISIBLE) {
            return "I" + SOURCE_PLAYER_ID + 1;
        }else{
            return "I" + SOURCE_PLAYER_ID + 0;
        }
    }

    @Override
    public void apply(GameSurface.GameThread gameThread) {
        gameThread.getPlayerArray()[SOURCE_PLAYER_ID].setInvisible(INVISIBLE);
    }
}
