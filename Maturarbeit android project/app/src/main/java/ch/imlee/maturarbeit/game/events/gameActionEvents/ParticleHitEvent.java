package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

/**
 * Created by Sandro on 23.08.2015.
 */
public class ParticleHitEvent extends GameActionEvent{

    private final int PARTICLE_ID;
    //if no player was hit: ID = -1
    private final byte PLAYER_HIT_ID;

    public ParticleHitEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        PARTICLE_ID = Integer.valueOf(eventString.substring(eventString.indexOf("p") + 1, eventString.indexOf("h")));
        PLAYER_HIT_ID = Byte.valueOf(eventString.substring(eventString.indexOf("h") + 1, eventString.indexOf("i")));
    }

    public ParticleHitEvent(int particleID, byte playerHitID, byte serverID){
        super(serverID);
        PARTICLE_ID = particleID;
        PLAYER_HIT_ID = playerHitID;
    }

    @Override
    public String toString() {
        return super.toString() + 'H' + 'p' + PARTICLE_ID+ 'h' + PLAYER_HIT_ID + 'i' + senderID;
    }

    @Override
    public void apply(GameThread gameThread) {
        gameThread.playerHit(PLAYER_HIT_ID);
        gameThread.removeParticle(PARTICLE_ID);
    }
}
