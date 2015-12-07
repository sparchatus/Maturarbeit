package ch.imlee.maturarbeit.events.gameStateEvents;

// this Event is sent by the host when he pauses the game. This Event is not implemented yet
public class GamePausedEvent extends GameStateEvent {

    @Override
    public String toString(){
        return super.toString() + 'P' + 'i' + senderID;
    }
    @Override
    public boolean handle(byte i){
        // TODO: implement
        return true;
    }
}
