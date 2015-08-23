package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

/**
 * Created by Sandro on 23.08.2015.
 */
public class ParticleHitEvent extends GameActionEvent{

    private final int PARTICLE_ID;
    //if no player was hit: ID = -1
    private final byte PLAYER_HIT_ID;

    public ParticleHitEvent(String string){
        PARTICLE_ID = Integer.valueOf(string.substring(string.indexOf("p") + 1, string.indexOf("h")));
        PLAYER_HIT_ID = Byte.valueOf(string.substring(string.indexOf("h") + 1));
    }

    public ParticleHitEvent(int particleID, byte playerHitID){
        PARTICLE_ID = particleID;
        PLAYER_HIT_ID = playerHitID;
    }

    @Override
    public String toString() {
        return super.toString() + 'H' + 'p' + PARTICLE_ID+ 'h' + PLAYER_HIT_ID;
    }

    @Override
    public void apply() {
        GameThread.removeParticle(PARTICLE_ID);
        GameThread.playerHit(PLAYER_HIT_ID);
    }
}
