package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

// this Event gets created and sent whenever a player shoots a particle
public class ParticleShotEvent extends GameActionEvent{

    // the spawn tick is used to calculate where the particle went during the time it took to send this Event
    public final int PARTICLE_ID,SPAWN_TICK;
    public final float X_COORDINATE, Y_COORDINATE;
    // the direction the particle is flying
    public final double ANGLE;

    public ParticleShotEvent(float xCoordinate, float yCoordinate, double angle, int particleID){
        super(GameThread.getUser().getID());
        X_COORDINATE = xCoordinate;
        Y_COORDINATE = yCoordinate;
        ANGLE = angle;
        SPAWN_TICK = GameThread.getSynchronizedTick();
        PARTICLE_ID = particleID;
    }

    public ParticleShotEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        X_COORDINATE = Float.valueOf(eventString.substring(eventString.indexOf('x') + 1, eventString.indexOf('y')));
        Y_COORDINATE = Float.valueOf(eventString.substring(eventString.indexOf('y') + 1, eventString.indexOf('a')));
        ANGLE = Double.valueOf(eventString.substring(eventString.indexOf('a') + 1, eventString.indexOf('s')));
        SPAWN_TICK = Integer.valueOf(eventString.substring(eventString.indexOf('s') + 1, eventString.indexOf('p')));
        PARTICLE_ID = Integer.valueOf(eventString.substring(eventString.indexOf('p') + 1, eventString.indexOf('i')));
    }

    @Override
    public String toString() {
        return super.toString() + 'P' + 'x' + X_COORDINATE + 'y' + Y_COORDINATE + 'a' + ANGLE + 's' + SPAWN_TICK + 'p' + PARTICLE_ID + 'i' + senderID;
    }
    @Override
    public void apply() {
        // add the particle to the list
        GameThread.addParticle(this);
    }
}
