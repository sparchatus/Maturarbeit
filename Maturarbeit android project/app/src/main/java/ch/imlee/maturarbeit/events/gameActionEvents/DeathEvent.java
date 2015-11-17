package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.map.Map;

// this Event gets created and sent when the user dies by falling into a void tile
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
