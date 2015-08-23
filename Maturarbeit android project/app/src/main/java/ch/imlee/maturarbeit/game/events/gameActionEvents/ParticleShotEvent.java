package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Particle;
import ch.imlee.maturarbeit.game.entity.User;

/**
 * Created by Lukas on 01.08.2015.
 */
public class ParticleShotEvent extends GameActionEvent {

    private final byte TEAM;
    private final float X_COORDINATE, Y_COORDINATE;
    private final double ANGLE;

    public ParticleShotEvent(User user){
        TEAM = user.TEAM;
        X_COORDINATE = user.getXCoordinate();
        Y_COORDINATE = user.getYCoordinate();
        ANGLE = user.getAngle();
        senderID = user.getID();
    }
    public ParticleShotEvent(String string){
        X_COORDINATE = Float.valueOf(string.substring(string.indexOf("x") + 1, string.indexOf("y")));
        Y_COORDINATE = Float.valueOf(string.substring(string.indexOf("y") + 1, string.indexOf("t")));
        TEAM = Byte.valueOf(string.substring(string.indexOf("t") + 1, string.indexOf("a")));
        ANGLE = Float.valueOf(string.substring(string.indexOf("a") + 1, string.length()-3));
        senderID = Byte.parseByte(string.substring(string.length()-1));
    }

    public String toString(){
        return super.toString() + 'P' + 'x' + X_COORDINATE + 'y' + Y_COORDINATE + 't' + TEAM + 'a' + ANGLE + 'i' + senderID;
    }

    public void apply(){
        GameThread.addParticle(new Particle(X_COORDINATE, Y_COORDINATE, TEAM, ANGLE));
    }
}
