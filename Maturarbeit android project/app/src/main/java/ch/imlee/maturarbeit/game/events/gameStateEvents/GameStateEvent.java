package ch.imlee.maturarbeit.game.events.gameStateEvents;

import ch.imlee.maturarbeit.game.events.Event;

/**
 * Created by Lukas on 18.06.2015.
 */
public class GameStateEvent extends Event {

    public GameStateEvent(byte senderID) {
        super(senderID);
    }

    public static GameStateEvent fromString(String eventString){
        switch(eventString.charAt(1)){
            case 'C': return new GameCancelledEvent(eventString);
            case 'L': return new GameLeftEvent(eventString);
            case 'l': return new GameLoadedEvent(eventString);
            case 'P': return new GamePausedEvent(eventString);
            case 'S': return new GameStartEvent(eventString);
            case 's': return new PlayerStatsSelectedEvent(eventString);
            default: return null;
        }
    }
    @Override
    public String toString(){
        return "G";
    }
}
