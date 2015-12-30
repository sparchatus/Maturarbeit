package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

// this Event gets created and sent when the user dies by falling into a void tile or by exploding after eating too many Sweets.
// it is also used when the player revives again.
public class DeathEvent extends GameActionEvent {

    private final boolean DEAD;

    public DeathEvent(boolean dead){
        super(GameThread.getUser().getID());
        DEAD = dead;
    }
    public DeathEvent(String eventString){
        DEAD = (1 == (Integer.parseInt(eventString.substring(eventString.indexOf('D')+1, eventString.indexOf('i')))));
        senderID = Byte.parseByte(eventString.substring(eventString.indexOf('i')+1));
    }

    @Override
    public String toString(){
        return super.toString()+ 'D' + ((DEAD) ? 1 : 0) + 'i' + senderID;
    }

    @Override
    public void apply(){
        GameThread.getPlayerArray()[senderID].setDead(DEAD);
    }
}
