package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.events.Event;

/**
 * Created by Lukas on 18.06.2015.
 */
public class GameActionEvent extends Event {

    public GameActionEvent(byte senderID) {
        super(senderID);
    }

    public GameActionEvent(){super();}

    public static GameActionEvent fromString(String eventString){
        switch(eventString.charAt(1)){
            case 'B': return new LightBulbEvent(eventString);
            case 'C': return new LightBulbServerEvent(eventString);
            case 'D': return new DeathEvent(eventString);
            case 'E': return new LightBulbStandEvent(eventString);
            case 'F': return new LightBulbStandServerEvent(eventString);
            case 'G': return new GameWinEvent(eventString);
            case 'H': return new ParticleHitEvent(eventString);
            case 'I': return new InvisibilityEvent(eventString);
            case 'L': return new SlimeEvent(eventString);
            case 'M': return new PlayerMotionEvent(eventString);
            case 'P': return new ParticleShotEvent(eventString);
            case 'R': return new RadiusChangedEvent(eventString);
            case 'S': return new StunEvent(eventString);
            case 'W': return new SweetSpawnEvent(eventString);
            case 'X': return new SweetEatenEvent(eventString);
            default:return null;
        }
    }

    @Override
    public String toString() {
        return "A";
    }

}
