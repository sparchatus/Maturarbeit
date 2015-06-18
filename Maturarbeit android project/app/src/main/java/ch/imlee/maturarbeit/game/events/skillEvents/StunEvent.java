package ch.imlee.maturarbeit.game.events.skillEvents;

import ch.imlee.maturarbeit.game.Player;
import ch.imlee.maturarbeit.game.events.Event;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 15.06.2015.
 */
public class StunEvent extends SkillEvent {

    public int playerID;

    public double stunTick;

    public StunEvent(Player player, double stunTick){
        playerID = player.getID();
        this.stunTick = stunTick;
    }

    public StunEvent(String eventString){
        char[] eventChar = eventString.toCharArray();
        playerID = eventChar[1];
        String stunTickString = "";
        for (int i = 2; i < eventChar.length; i ++){
            stunTick += eventChar[i];
        }
        stunTick = Integer.getInteger(stunTickString);
    }

    @Override
    public String toString() {
        return "S" + playerID + stunTick;
    }

    @Override
    public void apply(GameSurface.GameThread gameThread) {
        gameThread.getPlayerArray()[playerID].stun(stunTick);
    }
}
