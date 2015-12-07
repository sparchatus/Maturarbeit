package ch.imlee.maturarbeit.events.gameActionEvents;

import android.util.Log;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.utils.LogView;

// this just sends an error to the other device which might still be operable. it also helps collecting all errors on the computer;
public class ErrorEvent extends GameActionEvent{
    private final String POSITION;
    private final String ERROR;

    public ErrorEvent(String position, String error){
        super(GameThread.getUser().getID());
        POSITION = position;
        ERROR = error;
    }

    public ErrorEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        POSITION = eventString.substring(eventString.indexOf('_') + 1, eventString.indexOf('-'));
        ERROR = eventString.substring(eventString.indexOf('-') + 1, eventString.indexOf('='));
    }

    @Override
    public String toString() {
        return super.toString() + 'Z' + '_' + POSITION + '-' + ERROR + '=' + senderID;
    }

    @Override
    public void apply() {
        LogView.addLog("From: " + senderID + " == "  + ERROR);
        Log.e(POSITION, ERROR);
    }
}
