package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Sweet;

/**
 * Created by Lukas on 24.08.2015.
 */
public class SweetSpawnEvent extends GameActionEvent{
    private final int X, Y, ID;
    public SweetSpawnEvent(Sweet sweet){
        X = (int)sweet.getXCoordinate();
        Y = (int)sweet.getYCoordinate();
        ID = sweet.getID();
    }
    public SweetSpawnEvent(String eventString){
        X = Integer.parseInt(eventString.substring(eventString.indexOf('x')+1, eventString.indexOf('y')));
        Y = Integer.parseInt(eventString.substring(eventString.indexOf('y')+1, eventString.indexOf('i')));
        ID = Integer.parseInt(eventString.substring(eventString.indexOf('i')+1));
    }
    public String toString(){
        return super.toString() + "Wx" + X + 'y' + Y + 'i' + ID;
    }
    public void apply(GameThread gameThread){
        GameThread.sweets.add(new Sweet(X, Y, ID));
    }
}
