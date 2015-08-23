package ch.imlee.maturarbeit.game.events.gameStateEvents;

/**
 * Created by Lukas on 18.06.2015.
 */
public class GameLeftEvent extends GameStateEvent {

    public GameLeftEvent(byte senderID) {
        super(senderID);
    }

    public GameLeftEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
    }

    @Override
    public String toString(){
        return super.toString() + 'L' + 'i' + senderID;
    }
}
