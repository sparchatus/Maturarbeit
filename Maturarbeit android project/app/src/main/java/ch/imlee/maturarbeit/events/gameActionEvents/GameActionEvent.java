package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.events.Event;
import ch.imlee.maturarbeit.game.Tick;

public class GameActionEvent extends Event {

    public GameActionEvent(byte senderID) {
        super(senderID);
    }

    public GameActionEvent(){super();}

    public static GameActionEvent fromString(String eventString){
        // this method is used to get an actual event out of an eventString
        // the second character in each eventString determines its exact type
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
            case 'T': return new TickEvent(eventString);
            case 'W': return new SweetSpawnEvent(eventString);
            case 'X': return new SweetEatenEvent(eventString);
            // null only gets returned when the second character of the eventString got corrupted over bluetooth
            default: return null;
        }
    }

    @Override
    public String toString() {
        // this is the first character of every GameActionEvent's eventString
        return "A";
    }

}
