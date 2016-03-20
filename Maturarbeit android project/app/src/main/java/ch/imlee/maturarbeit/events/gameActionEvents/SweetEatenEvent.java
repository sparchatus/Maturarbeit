package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Sweet;

/*
this Event gets sent when a player came close to a Sweet and thus eating it
if the sender hasn't already reached the biggest possible radius, a RadiusChangedEvent will also get sent
*/
public class SweetEatenEvent extends GameActionEvent {
    private final int ID;
    public SweetEatenEvent(Sweet sweet){
        super(GameThread.getUser().getID());
        ID = sweet.getID();
    }
    public SweetEatenEvent(String eventString){
        super(Byte.parseByte(eventString.substring(eventString.indexOf('i') + 1)));
        ID = Integer.parseInt(eventString.substring(2, eventString.indexOf('i')));
    }

    @Override
    public String toString() {
        return super.toString() + 'X' + ID + 'i' + senderID;
    }
    @Override
    public void apply(){
        GameThread.sweetsToRemove.add(ID);
        GameThread.getPlayerArray()[senderID].sweetEaten();
    }
}
