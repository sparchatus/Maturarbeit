package ch.imlee.maturarbeit.events.gameStateEvents;

import ch.imlee.maturarbeit.activities.GameClient;
import ch.imlee.maturarbeit.views.GameSurface;

// this Event gets sent by the host when the game is finished and should be restarted
public class RestartGameEvent extends GameStateEvent {

    @Override
    public String toString(){
        return super.toString() + 'R';
    }

    @Override
    public boolean handle(byte i){
        GameSurface.restart();
        return true;
    }
}
