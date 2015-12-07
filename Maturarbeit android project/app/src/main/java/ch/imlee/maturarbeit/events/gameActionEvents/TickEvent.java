package ch.imlee.maturarbeit.events.gameActionEvents;


import ch.imlee.maturarbeit.game.GameThread;

public class TickEvent extends GameActionEvent{

    private final int SYNCHRONIZED_TICK;

    public TickEvent(){
        super((byte)0);
        SYNCHRONIZED_TICK = (int) GameThread.getSynchronizedTick();
    }

    public TickEvent(String eventString){
        super((byte)0);
        SYNCHRONIZED_TICK = Integer.valueOf(eventString.substring(eventString.indexOf('s') + 1, eventString.indexOf('i')));
    }

    @Override
    public String toString() {
        return super.toString() + 'T' + 's' + SYNCHRONIZED_TICK + 'i' + senderID;
    }

    @Override
    public void apply() {
        GameThread.setSynchronizedTick(SYNCHRONIZED_TICK);
    }
}
