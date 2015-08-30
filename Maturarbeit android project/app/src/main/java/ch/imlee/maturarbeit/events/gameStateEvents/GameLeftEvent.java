package ch.imlee.maturarbeit.events.gameStateEvents;

import ch.imlee.maturarbeit.game.GameThread;

/**
 * Created by Lukas on 18.06.2015.
 */
public class GameLeftEvent extends GameStateEvent {

    public GameLeftEvent() {
        super(GameThread.getUser().getID());
    }

    public GameLeftEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
    }

    @Override
    public String toString(){
        return super.toString() + 'L' + 'i' + senderID;
    }
}
