package ch.imlee.maturarbeit.events.gameStateEvents;

import ch.imlee.maturarbeit.events.Event;

public class GameStateEvent extends Event {

    public GameStateEvent(){}

    public GameStateEvent(byte senderID) {
        super(senderID);
    }

    public static GameStateEvent fromString(String eventString){
        // this switch / case is used to determine the exact type of an Event by its second character
        switch(eventString.charAt(1)){
            case 'C': return new GameCancelledEvent();
            case 'L': return new GameLeftEvent(eventString);
            case 'l': return new GameLoadedEvent(eventString);
            case 'P': return new GamePausedEvent();
            case 'S': return new GameStartEvent(eventString);
            case 's': return new PlayerStatsSelectedEvent(eventString);
            case 'R': return new RestartGameEvent();
            default: return null;
        }
    }

    @Override
    public String toString(){
        return "G";
    }
}
