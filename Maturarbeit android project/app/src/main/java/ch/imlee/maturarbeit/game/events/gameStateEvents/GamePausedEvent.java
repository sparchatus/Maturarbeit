package ch.imlee.maturarbeit.game.events.gameStateEvents;

/**
 * Created by Lukas on 18.06.2015.
 */
public class GamePausedEvent extends GameStateEvent {
    @Override
    public String toString(){
        return super.toString() + 'P';
    }
}
