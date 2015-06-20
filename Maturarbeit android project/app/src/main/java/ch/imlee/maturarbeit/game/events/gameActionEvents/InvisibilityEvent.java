package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 18.06.2015.
 */
public class InvisibilityEvent extends GameActionEvent{
    public final byte PLAYER_ID;
    public final boolean INVISIBLE;

    public InvisibilityEvent(String eventString){
        PLAYER_ID = Byte.valueOf(eventString.substring(2, 3));
        if (Byte.valueOf(eventString.substring(2)) == 0) {
            INVISIBLE = false;
        }else {
            INVISIBLE = true;
        }
    }

    public InvisibilityEvent(byte playerId, boolean invisible){
        PLAYER_ID = playerId;
        INVISIBLE = invisible;
    }

    @Override
    public String toString() {
        if (INVISIBLE) {
            return super.toString() + "I" + PLAYER_ID + 1;
        }else{
            return super.toString() + "I" + PLAYER_ID + 0;
        }
    }

    @Override
    public void apply(GameSurface.GameThread gameThread) {
        gameThread.getPlayerArray()[PLAYER_ID].setInvisible(INVISIBLE);
    }
}
