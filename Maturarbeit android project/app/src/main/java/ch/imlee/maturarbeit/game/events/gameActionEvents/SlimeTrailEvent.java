package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.SlimeTrail;

/**
 * Created by Lukas on 21.08.2015.
 */
public class SlimeTrailEvent extends GameActionEvent{
    private final float X, Y;

    public SlimeTrailEvent(SlimeTrail slimeTrail){
        X = slimeTrail.getXCoordinate();
        Y = slimeTrail.getYCoordinate();
    }
    public SlimeTrailEvent(String string){
        X = Float.parseFloat(string.substring(3, string.indexOf('y')));
        Y = Float.parseFloat(string.substring(string.indexOf('y') + 1));
    }
    @Override
    public String toString(){
        return super.toString() + "Lx" + X + 'y' + Y;
    }
    @Override
    public void apply(){
        GameThread.addSlimeTrail(new SlimeTrail(X, Y));
    }
}
