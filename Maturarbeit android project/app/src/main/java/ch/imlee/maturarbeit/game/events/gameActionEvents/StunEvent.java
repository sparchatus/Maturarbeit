package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 15.06.2015.
 */
public class StunEvent extends GameActionEvent {

    public int playerID;

    public double stunTick;

    public StunEvent(Player player, double stunTick){
        playerID = player.getID();
        this.stunTick = stunTick;
    }

    public StunEvent(String eventString){
        char[] eventChar = eventString.toCharArray();
        playerID = eventChar[2];
        stunTick = Double.valueOf(eventString.substring(3));
    }

    @Override
    public String toString() {
        return super.toString() + "S" + playerID + stunTick;
    }

    @Override
    public void apply(GameSurface.GameThread gameThread) {
        gameThread.getPlayerArray()[playerID].stun(stunTick);
    }
}
