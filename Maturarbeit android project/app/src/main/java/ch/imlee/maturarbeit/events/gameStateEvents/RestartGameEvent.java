package ch.imlee.maturarbeit.events.gameStateEvents;

/**
 * Created by Lukas on 19.09.2015.
 */
public class RestartGameEvent extends GameStateEvent {
    @Override
    public String toString(){
        return super.toString() + 'R';
    }

    @Override
    public boolean handle(byte i){

        return true;
    }
}
