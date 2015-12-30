package ch.imlee.maturarbeit.events.gameActionEvents;

import android.util.Log;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.utils.LogView;

// This Event is used to send any message, especially error logs, to the other devices. This System is of use when the Android Log isn't working.
public class ErrorEvent extends GameActionEvent{
    private final String ERROR;

    /**
     *
     * @param error - the message to deliver
     * @param i - a variable to distinct between the two different constructors
     */
    public ErrorEvent(String error, int i){
        super(GameThread.getUser().getID());
        ERROR = error;
    }

    public ErrorEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        ERROR = eventString.substring(eventString.indexOf('e') + 1, eventString.indexOf('\n'));
    }

    @Override
    public String toString() {
        return super.toString() + 'Z' + 'e' + ERROR + '\n' + 'i' + senderID;
    }

    @Override
    public void apply() {
        LogView.addLog("From: " + senderID + " == "  + ERROR);
        Log.e("ErrorEvent", ERROR);
    }
}
