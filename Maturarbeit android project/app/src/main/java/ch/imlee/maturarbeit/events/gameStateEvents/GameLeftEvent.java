package ch.imlee.maturarbeit.events.gameStateEvents;

import ch.imlee.maturarbeit.game.GameThread;

// This method is sent by the host when he loses the connection to one of the clients. This Event is not implemented yet
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
    @Override
    public boolean handle(byte i){
        // TODO: implement
        return true;
    }
}
