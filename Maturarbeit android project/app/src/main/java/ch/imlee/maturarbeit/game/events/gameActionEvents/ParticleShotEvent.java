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
    private final int SPAWN_TICK;
    private final int PARTICLE_ID;

    public ParticleShotEvent(String string){
        X_COORDINATE = Float.valueOf(string.substring(string.indexOf("x") + 1, string.indexOf("y")));
        Y_COORDINATE = Float.valueOf(string.substring(string.indexOf("y") + 1, string.indexOf("t")));
        TEAM = Byte.valueOf(string.substring(string.indexOf("t") + 1, string.indexOf("a")));
        ANGLE = Float.valueOf(string.substring(string.indexOf("a") + 1, string.indexOf('s')));
        SPAWN_TICK = Integer.valueOf(string.substring(string.indexOf("s") + 1, string.indexOf("p")));
        PARTICLE_ID = Integer.valueOf(string.substring(string.indexOf("p") + 1));
    }

    public ParticleShotEvent(ParticleServerEvent particleServerEvent, int particleID){
        TEAM = particleServerEvent.getTEAM();
        X_COORDINATE = particleServerEvent.getX_COORDINATE();
        Y_COORDINATE = particleServerEvent.getX_COORDINATE();
        ANGLE = particleServerEvent.getANGLE();
        SPAWN_TICK = particleServerEvent.getSPAW_TICK();
        PARTICLE_ID = particleID;
    }

    public ParticleShotEvent (User user, int spawnTick, int particleID) {
        TEAM = user.TEAM;
        X_COORDINATE = user.getXCoordinate();
        Y_COORDINATE = user.getYCoordinate();
        ANGLE = user.getAngle();
        SPAWN_TICK = spawnTick;
        PARTICLE_ID = particleID;
    }

    @Override
    public String toString() {
        return super.toString() + 'P' + 'x' + X_COORDINATE + 'y' + Y_COORDINATE + 't' + TEAM + 'a' + ANGLE + 's' + SPAWN_TICK + 'p' + PARTICLE_ID;
    }

    @Override
    public void apply() {
        GameThread.addParticle(new Particle(X_COORDINATE, Y_COORDINATE, TEAM, ANGLE, SPAWN_TICK, PARTICLE_ID));
    }
}
