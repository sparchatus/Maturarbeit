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
    private final double SPAWN_TICK;

    public ParticleServerEvent(User user, double spawnTick){
        super(user.getID());
        TEAM = user.TEAM;
        SPAWN_TICK = spawnTick;
        X_COORDINATE = user.getXCoordinate();
        Y_COORDINATE = user.getYCoordinate();
        ANGLE = user.getAngle();
    }
    public ParticleServerEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        X_COORDINATE = Float.valueOf(eventString.substring(eventString.indexOf("x") + 1, eventString.indexOf("y")));
        Y_COORDINATE = Float.valueOf(eventString.substring(eventString.indexOf("y") + 1, eventString.indexOf("t")));
        TEAM = Byte.valueOf(eventString.substring(eventString.indexOf("t") + 1, eventString.indexOf("a")));
        ANGLE = Float.valueOf(eventString.substring(eventString.indexOf("a") + 1, eventString.indexOf('s')));
        SPAWN_TICK = Double.valueOf(eventString.substring(eventString.indexOf("s") + 1, eventString.indexOf("i")));
    }

    public String toString(){
        return super.toString() + 'Q' + 'x' + X_COORDINATE + 'y' + Y_COORDINATE + 't' + TEAM + 'a' + ANGLE + 's' + SPAWN_TICK + "i" + senderID;
    }

    public void apply(GameThread gameThread){
        int particleID = GameServerThread.getCurrentParticleID();
        new ParticleShotEvent(this, particleID).send();
        gameThread.addParticle(new Particle(X_COORDINATE, Y_COORDINATE, TEAM, ANGLE, SPAWN_TICK, particleID));
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

    public double getSPAWN_TICK() {
        return SPAWN_TICK;
    }
}
