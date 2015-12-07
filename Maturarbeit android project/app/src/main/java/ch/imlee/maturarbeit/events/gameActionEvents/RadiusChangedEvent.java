package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

// this Event gets sent whenever a player changes his radius, either as a visual effect when falling or when gaining or losing weight
public class RadiusChangedEvent extends GameActionEvent {
    private final float RADIUS;
    public RadiusChangedEvent(float radius){
        super(GameThread.getUser().getID());
        RADIUS = radius;
    }
    public RadiusChangedEvent(String eventString){
        super(Byte.parseByte(eventString.substring(eventString.indexOf('i') + 1)));
        RADIUS = Float.parseFloat(eventString.substring(2, eventString.indexOf('i')));
    }

    @Override
    public String toString(){
        return super.toString() + 'R' + RADIUS + 'i' + senderID;
    }
    @Override
    public void apply(){
        GameThread.getPlayerArray()[senderID].setPlayerRadius(RADIUS);
    }
}
