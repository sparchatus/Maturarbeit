package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Particle;
import ch.imlee.maturarbeit.game.entity.User;

/**
 * Created by Lukas on 01.08.2015.
 */
public class ParticleShotEvent extends GameActionEvent {

    final byte TEAM;
    public ParticleShotEvent(byte id){
        TEAM = id;
    }
    public ParticleShotEvent(String string){
        TEAM = Byte.parseByte(string.substring(string.length()-1));
    }

    public String toString(){
        return super.toString() + 'P' + TEAM;
    }

    public void apply(){
        GameThread.addParticle(new Particle(GameThread.getPlayerArray()[TEAM]));
    }
}
