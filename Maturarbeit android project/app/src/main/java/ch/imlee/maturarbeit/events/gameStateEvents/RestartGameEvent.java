package ch.imlee.maturarbeit.events.gameStateEvents;

// this Event gets sent by the host when the game is finished and should be restarted. It is not implemented yet
public class RestartGameEvent extends GameStateEvent {

    @Override
    public String toString(){
        return super.toString() + 'R';
    }

    @Override
    public boolean handle(byte i){
        // TODO: implement
        return true;
    }
}
