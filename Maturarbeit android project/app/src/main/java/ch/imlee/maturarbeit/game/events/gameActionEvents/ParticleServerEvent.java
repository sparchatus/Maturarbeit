package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameServerThread;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Particle;
import ch.imlee.maturarbeit.game.entity.User;

/**
 * Created by Sandro on 23.08.2015.
 */
public class ParticleServerEvent extends GameActionEvent{
    private final byte TEAM;
    private final float X_COORDINATE, Y_COORDINATE;
    private final double ANGLE;
    private final int SPAWN_TICK;

    public ParticleServerEvent(User user, int spawTick){
        TEAM = user.TEAM;
        SPAWN_TICK = spawTick;
        X_COORDINATE = user.getXCoordinate();
        Y_COORDINATE = user.getYCoordinate();
        ANGLE = user.getAngle();
    }
    public ParticleServerEvent(String string){
        X_COORDINATE = Float.valueOf(string.substring(string.indexOf("x") + 1, string.indexOf("y")));
        Y_COORDINATE = Float.valueOf(string.substring(string.indexOf("y") + 1, string.indexOf("t")));
        TEAM = Byte.valueOf(string.substring(string.indexOf("t") + 1, string.indexOf("a")));
        ANGLE = Float.valueOf(string.substring(string.indexOf("a") + 1, string.indexOf('s')));
        SPAWN_TICK = Integer.valueOf(string.substring(string.indexOf("s") + 1));
    }

    public String toString(){
        return super.toString() + 'Q' + 'x' + X_COORDINATE + 'y' + Y_COORDINATE + 't' + TEAM + 'a' + ANGLE + 's' + SPAWN_TICK;
    }

    public void apply(){
        int particleID = GameServerThread.getCurrentParticleID();
        new ParticleShotEvent(this, particleID).send();
        GameThread.addParticle(new Particle(X_COORDINATE, Y_COORDINATE, TEAM, ANGLE, SPAWN_TICK, particleID));
    }

    public byte getTEAM(){
        return TEAM;
    }

    public double getANGLE() {
        return ANGLE;
    }

    public float getX_COORDINATE() {
        return X_COORDINATE;
    }

    public float getY_COORDINATE() {
        return Y_COORDINATE;
    }

    public int getSPAW_TICK() {
        return SPAWN_TICK;
    }
}
