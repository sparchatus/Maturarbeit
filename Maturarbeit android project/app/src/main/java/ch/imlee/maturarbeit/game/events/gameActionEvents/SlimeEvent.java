package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Sound.SlimeSound;
import ch.imlee.maturarbeit.game.entity.Slime;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 18.06.2015.
 */
public class SlimeEvent extends GameActionEvent{

    private boolean slimy;

    private final byte PLAYER_ID;

    public SlimeEvent(String eventString){
        PLAYER_ID = Byte.valueOf(eventString.substring(2, 3));
        if (eventString.substring(3).equals("0")){
            slimy = false;
        }else {
            slimy = true;
        }
    }

    public SlimeEvent(byte playerId, boolean slimy){
        PLAYER_ID = playerId;
        this.slimy = slimy;
    }

    public void setSlimy(boolean slimy) {
        this.slimy = slimy;
    }


    @Override
    public String toString() {
        if (slimy) {
            return super.toString() + "L" + PLAYER_ID + '1';
        }else {
            return super.toString() + "L" + PLAYER_ID + '0';
        }
    }

    @Override
    public void apply() {
        GameThread.getPlayerArray()[PLAYER_ID].setSlimy(slimy);
        if(slimy){
            GameThread.getPlayerArray()[PLAYER_ID].getSlimeSound().start();
        } else{
            GameThread.getPlayerArray()[PLAYER_ID].getSlimeSound().stop();
        }
    }
}
