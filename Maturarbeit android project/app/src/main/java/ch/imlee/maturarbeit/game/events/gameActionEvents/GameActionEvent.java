package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.events.Event;
import ch.imlee.maturarbeit.game.events.EventType;

/**
 * Created by Lukas on 18.06.2015.
 */
public class GameActionEvent extends Event {

    public static GameActionEvent fromString(String string){
        switch(string.charAt(1)){
            case 'B': return new LightBulbEvent(string);
            case 'C': return new LightBulbServerEvent(string);
            case 'H': return new ParticleHitEvent(string);
            case 'I': return new InvisibilityEvent(string);
            case 'L': return new SlimeTrailEvent(string);
            case 'M': return new PlayerMotionEvent(string);
            case 'P': return new ParticleShotEvent(string);
            case 'Q': return new ParticleServerEvent(string);
            case 'S': return new StunEvent(string);
            default:return null;
        }
    }

    @Override
    public String toString() {
        return "A";
    }

}
