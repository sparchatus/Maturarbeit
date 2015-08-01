package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Slime;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 18.06.2015.
 */
public class SlimeEvent extends GameActionEvent{

    private final boolean SLIMY;

    private final byte PLAYER_ID;

    public SlimeEvent(String eventString){
        PLAYER_ID = Byte.valueOf(eventString.substring(2, 3));
        if (eventString.substring(3).equals("0")){
            SLIMY = false;
        }else {
            SLIMY = true;
        }
    }

    public SlimeEvent(byte playerId, boolean slimy){
        PLAYER_ID = playerId;
        SLIMY = slimy;
    }

    @Override
    public String toString() {
        if (SLIMY) {
            return super.toString() + "L" + PLAYER_ID + 0;
        }else {
            return super.toString() + "L" + PLAYER_ID + 1;
        }
    }

    @Override
    public void apply(GameThread gameThread) {
        gameThread.getPlayerArray()[PLAYER_ID].setSlimy(SLIMY);
    }
}
