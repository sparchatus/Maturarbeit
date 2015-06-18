package ch.imlee.maturarbeit.game.events;

import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 15.06.2015.
 */
public class StunEvent extends Event{

    public int playerID;

    public double stunTick;

    public StunEvent(Player player, double stunTick){
        playerID = player.getID();
        this.stunTick = stunTick;
    }

    public StunEvent(String eventString){
        char[] eventChar = eventString.toCharArray();
        playerID = eventChar[1];
        stunTick = Double.valueOf(eventString.substring(2));
    }

    @Override
    public String toEventString() {
        return "S" + playerID + stunTick;
    }

    @Override
    public void apply(GameSurface.GameThread gameThread) {
        gameThread.getPlayerArray()[playerID].stun(stunTick);
    }
}
