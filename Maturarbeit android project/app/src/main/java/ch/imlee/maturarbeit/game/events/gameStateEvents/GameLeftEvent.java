package ch.imlee.maturarbeit.game.events.gameStateEvents;

/**
 * Created by Lukas on 18.06.2015.
 */
public class GameLeftEvent extends GameStateEvent {
    public GameLeftEvent(String string){

    }
    @Override
    public String toString(){
        return super.toString() + 'L';
    }
}
