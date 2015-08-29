package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Sweet;

/**
 * Created by Lukas on 26.08.2015.
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
        return super.toString() + 'E' + ID + 'i' + senderID;
    }
    @Override
    public void apply(GameThread gameThread){
        GameThread.sweetsToRemove.add(ID);
    }
}
