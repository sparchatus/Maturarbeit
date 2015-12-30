package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

// this Event gets created and sent by a Ghost that switches between invisible and visible
public class InvisibilityEvent extends GameActionEvent{
    public final boolean INVISIBLE;

    public InvisibilityEvent(boolean invisible){
        super(GameThread.getUser().getID());
        INVISIBLE = invisible;
    }

    public InvisibilityEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        INVISIBLE = (eventString.charAt(2) == '1');
    }

    @Override
    public String toString() {
        return super.toString() + 'I' + ((INVISIBLE) ? 1 : 0) + 'i' + senderID;
    }

    @Override
    public void apply() {
        GameThread.getPlayerArray()[senderID].setInvisible(INVISIBLE);
    }
}
