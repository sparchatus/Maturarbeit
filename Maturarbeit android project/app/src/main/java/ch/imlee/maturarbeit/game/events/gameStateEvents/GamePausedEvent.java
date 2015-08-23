package ch.imlee.maturarbeit.game.events.gameStateEvents;

/**
 * Created by Lukas on 18.06.2015.
 */
public class GamePausedEvent extends GameStateEvent {
    public GamePausedEvent(byte senderID) {
        super(senderID);
    }

    public GamePausedEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
    }

    @Override
    public String toString(){
        return super.toString() + 'P' + 'i' + senderID;
    }
}
