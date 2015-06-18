package ch.imlee.maturarbeit.game.events.gameStateEvents;

import ch.imlee.maturarbeit.game.events.Event;

/**
 * Created by Lukas on 17.06.2015.
 */
public class GameStartEvent extends GameStateEvent {
    @Override
    public String toString(){
        return super.toString() + 'S';
    }
}
