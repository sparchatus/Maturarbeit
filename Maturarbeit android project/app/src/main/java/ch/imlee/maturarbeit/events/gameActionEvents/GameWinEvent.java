package ch.imlee.maturarbeit.events.gameActionEvents;

import android.util.Log;
import ch.imlee.maturarbeit.game.GameThread;

// this Event gets created and sent by the host in the LightBulbStandServerEvent's apply method when a team wins by having two LightBulbs in their base
public class GameWinEvent extends GameActionEvent {

    // the winning team
    private final byte TEAM;

    public GameWinEvent(byte team){
        TEAM = team;
    }
    public GameWinEvent(String eventString){
        TEAM = Byte.parseByte(eventString.substring(2));
    }

    @Override
    public String toString(){
        return super.toString() + 'G' + TEAM;
    }
    @Override
    public void apply(){
        Log.i("game ended", "Team " + TEAM + " won!");
        GameThread.setWinningTeam(TEAM);
        GameThread.setRunning(false);
        GameThread.activateEndGame();
    }
}
