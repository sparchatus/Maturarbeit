package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

/**
 * Created by Sandro on 15.06.2015.
 */
public class StunEvent extends GameActionEvent {

    public double stunTick;
    public byte stunnedPlayerID;

    public StunEvent(byte playerID,byte stunnedID, double stunTick){
        super(playerID);
        this.stunTick = stunTick;
        stunnedPlayerID = stunnedID;
    }

    public StunEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        stunTick = Double.valueOf(eventString.substring(eventString.indexOf("s") + 1, eventString.indexOf("i")));
        stunnedPlayerID = Byte.valueOf(eventString.substring(eventString.indexOf("p") + 1, eventString.indexOf("s")));
    }

    @Override
    public String toString() {
        return super.toString() + "S" + 'p'+ stunnedPlayerID + 's' + stunTick + 'i' + senderID;
    }

    @Override
    public void apply() {
        GameThread.getPlayerArray()[stunnedPlayerID].stun(stunTick);
    }
}
