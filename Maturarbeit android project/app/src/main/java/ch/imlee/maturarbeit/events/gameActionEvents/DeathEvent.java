package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.map.Map;

/**
 * Created by Lukas on 09.09.2015.
 */
public class DeathEvent extends GameActionEvent {
    private final boolean DEAD;

    public DeathEvent(boolean dead){
        super(GameThread.getUser().getID());
        DEAD = dead;
    }
    public DeathEvent(String eventString){
        DEAD = (1 == (Integer.parseInt(eventString.substring(3,4))));
        senderID = Byte.parseByte(eventString.substring(eventString.indexOf('i')));
    }

    @Override
    public String toString(){
        return super.toString()+ 'D' + ((DEAD) ? 1 : 0) + 'i' + senderID;
    }
    @Override
    public void apply(){
        Player player = GameThread.getPlayerArray()[senderID];
        player.setDead(DEAD);
        if(DEAD){
            player.setCoordinates(Map.getStartX(player.TEAM), Map.getStartY(player.TEAM));
            player.bulbLost();
        }
    }
}
