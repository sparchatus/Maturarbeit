package ch.imlee.maturarbeit.events.gameStateEvents;

// this Event is sent by the host when he pauses the game. This Event is not implemented yet
public class GamePausedEvent extends GameStateEvent {

    //TODO: implement later
    @Override
    public String toString(){
        return super.toString() + 'P';
    }
    @Override
    public boolean handle(byte i){
        return true;
    }
}
