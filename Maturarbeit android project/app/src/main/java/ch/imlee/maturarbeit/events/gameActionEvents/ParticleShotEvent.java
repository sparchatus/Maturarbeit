package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.activities.GameClient;
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
    private final double SPAWN_TICK;
    private final int PARTICLE_ID;

    public ParticleShotEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        X_COORDINATE = Float.valueOf(eventString.substring(eventString.indexOf("x") + 1, eventString.indexOf("y")));
        Y_COORDINATE = Float.valueOf(eventString.substring(eventString.indexOf("y") + 1, eventString.indexOf("t")));
        TEAM = Byte.valueOf(eventString.substring(eventString.indexOf("t") + 1, eventString.indexOf("a")));
        ANGLE = Float.valueOf(eventString.substring(eventString.indexOf("a") + 1, eventString.indexOf("s")));
        SPAWN_TICK = Integer.valueOf(eventString.substring(eventString.indexOf("s") + 1, eventString.indexOf("p")));
        PARTICLE_ID = Integer.valueOf(eventString.substring(eventString.indexOf("p") + 1), eventString.indexOf("i"));
    }

    public ParticleShotEvent(ParticleServerEvent particleServerEvent, int particleID){
        super(particleServerEvent.getSenderID());
        TEAM = particleServerEvent.getTEAM();
        X_COORDINATE = particleServerEvent.getX_COORDINATE();
        Y_COORDINATE = particleServerEvent.getY_COORDINATE();
        ANGLE = particleServerEvent.getANGLE();
        SPAWN_TICK = particleServerEvent.getSPAWN_TICK();
        PARTICLE_ID = particleID;
    }

    public ParticleShotEvent (User user, int spawnTick, int particleID) {
        super(user.getID());
        TEAM = user.TEAM;
        X_COORDINATE = user.getXCoordinate();
        Y_COORDINATE = user.getYCoordinate();
        ANGLE = user.getAngle();
        SPAWN_TICK = spawnTick;
        PARTICLE_ID = particleID;
    }

    @Override
    public String toString() {
        return super.toString() + 'P' + 'x' + X_COORDINATE + 'y' + Y_COORDINATE + 't' + TEAM + 'a' + ANGLE + 's' + SPAWN_TICK + 'p' + PARTICLE_ID + 'i' + senderID;
    }

    @Override
    public void apply() {
        GameClient.getGameThread().addParticle(new Particle(X_COORDINATE, Y_COORDINATE, TEAM, ANGLE, SPAWN_TICK, PARTICLE_ID));
    }
}
