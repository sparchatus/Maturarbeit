package ch.imlee.maturarbeit.game.events;

import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 15.06.2015.
 */
public class PlayerMotionEvent extends Event{

    private final float X_COORDINATE, Y_COORDINATE;
    private final double ANGLE;

    private final byte ID;

    public PlayerMotionEvent(String eventString){
        X_COORDINATE = Float.valueOf(eventString.substring(eventString.indexOf("x") + 1, eventString.indexOf("y")));
        Y_COORDINATE = Float.valueOf(eventString.substring(eventString.indexOf("y") + 1, eventString.indexOf("a")));
        ANGLE = Double.valueOf(eventString.substring(eventString.indexOf("a") + 1, eventString.indexOf("i")));
        ID = Byte.valueOf(eventString.substring(eventString.length() - 1));
    }

    public PlayerMotionEvent(float xCoordinate, float yCoordinate, double angle, byte playerId){
        X_COORDINATE = xCoordinate;
        Y_COORDINATE = yCoordinate;
        ANGLE = angle;
        ID = playerId;
    }

    @Override
    public String toEventString() {
        return "P" + "x" + X_COORDINATE + "y" + Y_COORDINATE + "a" + ANGLE + "i" + ID;
    }

    @Override
    public void apply(GameSurface.GameThread gameThread) {

    }
}
