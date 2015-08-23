package ch.imlee.maturarbeit.game.events.gameStateEvents;

/**
 * Created by Lukas on 18.06.2015.
 */
public class GameCancelledEvent extends GameStateEvent {

    public GameCancelledEvent(byte senderID) {
        super(senderID);
    }

    public GameCancelledEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
    }

    @Override
    public String toString(){
        return super.toString() + 'C' + 'i' + senderID;
    }
}
