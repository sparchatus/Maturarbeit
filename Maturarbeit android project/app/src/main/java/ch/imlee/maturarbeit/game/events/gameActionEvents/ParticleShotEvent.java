package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Particle;
import ch.imlee.maturarbeit.game.entity.User;

/**
 * Created by Lukas on 01.08.2015.
 */
public class ParticleShotEvent extends GameActionEvent {
    final byte ID;

    public ParticleShotEvent(byte id){
        ID = id;
    }
    public ParticleShotEvent(String string){
        ID = Byte.parseByte(string.substring(string.length()-1));
    }

    public String toString(){
        return super.toString() + 'P' + ID;
    }

    public void apply(GameThread gameThread){
        gameThread.addParticle(new Particle(gameThread.getPlayerArray()[ID], gameThread));
    }
}
