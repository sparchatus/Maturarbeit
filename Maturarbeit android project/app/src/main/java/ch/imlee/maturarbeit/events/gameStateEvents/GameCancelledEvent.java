package ch.imlee.maturarbeit.events.gameStateEvents;

import ch.imlee.maturarbeit.activities.GameClient;
import ch.imlee.maturarbeit.views.GameSurface;

// this game is sent by the host when he ends an ongoing game. This Event is not implemented yet
public class GameCancelledEvent extends GameStateEvent {

    @Override
    public String toString(){
        return super.toString() + 'C';
    }
    @Override
    public boolean handle(byte i){
        // TODO: implement
        return true;
    }
}
