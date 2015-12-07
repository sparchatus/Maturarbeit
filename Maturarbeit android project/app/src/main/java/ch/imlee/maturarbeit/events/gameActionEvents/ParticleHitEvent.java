package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Sound.Sound;

// this Event gets sent by the host when a Particle has hit either a wall or a player
public class ParticleHitEvent extends GameActionEvent{

    public final byte PLAYER_SOURCE_ID;
    public final int PARTICLE_ID;
    //-1 when a wall is hit, otherwise the ID of the player that got hit
    public final byte PLAYER_HIT_ID;

    public ParticleHitEvent(int particleID, byte playerHitID, byte playerSourceID){
        PARTICLE_ID = particleID;
        PLAYER_HIT_ID = playerHitID;
        PLAYER_SOURCE_ID = playerSourceID;
    }
    public ParticleHitEvent(String eventString){
        PARTICLE_ID = Integer.valueOf(eventString.substring(eventString.indexOf('p') + 1, eventString.indexOf('h')));
        PLAYER_HIT_ID = Byte.valueOf(eventString.substring(eventString.indexOf('h') + 1, eventString.indexOf('s')));
        PLAYER_SOURCE_ID = Byte.valueOf(eventString.substring(eventString.indexOf('s') + 1, eventString.indexOf('i')));
    }

    @Override
    public String toString() {
        return super.toString() + 'H' + 'p' + PARTICLE_ID + 'h' + PLAYER_HIT_ID + 's' + PLAYER_SOURCE_ID + 'i' + senderID;
    }
    @Override
    public void apply() {
        GameThread.removeParticle(this);
        if (PLAYER_HIT_ID !=-1){
            GameThread.getPlayerArray()[PLAYER_HIT_ID].particleHit();
        }
        Sound.play(Sound.PARTICLE_HIT);
    }
}
