package ch.imlee.maturarbeit.game.events.gameStateEvents;

import ch.imlee.maturarbeit.game.events.Event;

/**
 * Created by Lukas on 18.06.2015.
 */
public class GameStateEvent extends Event {
    public static GameStateEvent fromString(String string){
        switch(string.charAt(1)){
            case 'C': return new GameCancelledEvent();
            case 'L': return new GameLeftEvent(string);
            case 'l': return new GameLoadedEvent();
            case 'P': return new GamePausedEvent();
            case 'S': return new GameStartEvent(string);
            case 's': return new PlayerStatsSelectedEvent(string);
            default: return null;
        }
    }
    @Override
    public String toString(){
        return "G";
    }
}
