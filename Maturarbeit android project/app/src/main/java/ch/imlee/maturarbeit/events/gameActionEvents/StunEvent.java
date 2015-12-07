package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

// this Event gets sent by a Fluffy that activates his special skill an thus stuns another player for a certain amount of time
public class StunEvent extends GameActionEvent {

    public final double STUN_TICK;
    public final byte STUNNED_PLAYER_ID;

    public StunEvent(byte playerID,byte stunnedID, double stunTick){
        super(playerID);
        STUN_TICK = stunTick;
        STUNNED_PLAYER_ID = stunnedID;
    }
    public StunEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        STUN_TICK = Double.valueOf(eventString.substring(eventString.indexOf('s') + 1, eventString.indexOf('i')));
        STUNNED_PLAYER_ID = Byte.valueOf(eventString.substring(eventString.indexOf('p') + 1, eventString.indexOf('s')));
    }

    @Override
    public String toString() {
        return super.toString() + 'S' + 'p'+ STUNNED_PLAYER_ID + 's' + STUN_TICK + 'i' + senderID;
    }
    @Override
    public void apply() {
        GameThread.getPlayerArray()[STUNNED_PLAYER_ID].stun(STUN_TICK);
    }
}
