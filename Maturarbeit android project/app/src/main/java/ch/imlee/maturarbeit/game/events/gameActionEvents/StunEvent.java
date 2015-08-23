package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 15.06.2015.
 */
public class StunEvent extends GameActionEvent {

    public double stunTick;

    public StunEvent(byte playerID, double stunTick){
        senderID = playerID;
        this.stunTick = stunTick;
    }

    public StunEvent(String eventString){
        char[] eventChar = eventString.toCharArray();
        senderID = (byte)Character.getNumericValue(eventChar[2]);
        stunTick = Double.valueOf(eventString.substring(3));
    }

    @Override
    public String toString() {
        return super.toString() + "S" + senderID + stunTick;
    }

    @Override
    public void apply() {
        GameThread.getPlayerArray()[senderID].stun(stunTick);
    }
}
