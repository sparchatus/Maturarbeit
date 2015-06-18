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
        char[]eventChar = eventString.toCharArray();
        X_COORDINATE = 0;
        Y_COORDINATE = 0;
        ANGLE = 0;
        ID = 0;
    }

    public PlayerMotionEvent(float xCoordinate, float yCoordinate, double angle, byte playerId){
        X_COORDINATE = xCoordinate;
        Y_COORDINATE = yCoordinate;
        ANGLE = angle;
        ID = playerId;
    }

    @Override
    public String toString() {
        return "P";
    }

    @Override
    public void apply(GameSurface.GameThread gameThread) {

    }
}
