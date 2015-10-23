package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

/**
 * Created by Sandro on 22.10.2015.
 */
public class TickEvent extends GameActionEvent{

    private final int SYNCHRONIZED_TICK;

    public TickEvent() {
        super((byte)0);
        SYNCHRONIZED_TICK = (int)GameThread.getSynchronizedTick();
    }

    public TickEvent (String eventString){
        super((byte)0);
        SYNCHRONIZED_TICK = Byte.valueOf(eventString.substring(eventString.indexOf("s") + 1));
    }

    @Override
    public String toString() {
        return super.toString() + 'T' + 's' + SYNCHRONIZED_TICK;
    }

    @Override
    public void apply() {
        GameThread.renewTick(SYNCHRONIZED_TICK);
    }
}
