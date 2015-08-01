package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

/**
 * Created by Sandro on 15.06.2015.
 */
public class PlayerMotionEvent extends GameActionEvent {

    private final float X_COORDINATE, Y_COORDINATE;
    private final double ANGLE;

    private final byte PLAYER_ID;

    public PlayerMotionEvent(String eventString){
        X_COORDINATE = Float.valueOf(eventString.substring(eventString.indexOf("x") + 1, eventString.indexOf("y")));
        Y_COORDINATE = Float.valueOf(eventString.substring(eventString.indexOf("y") + 1, eventString.indexOf("a")));
        ANGLE = Double.valueOf(eventString.substring(eventString.indexOf("a") + 1, eventString.indexOf("i")));
        PLAYER_ID = Byte.valueOf(eventString.substring(eventString.length() - 1));
    }

    public PlayerMotionEvent(float xCoordinate, float yCoordinate, double angle, byte playerId){
        X_COORDINATE = xCoordinate;
        Y_COORDINATE = yCoordinate;
        ANGLE = angle;
        PLAYER_ID = playerId;
    }

    @Override
    public String toString() {
        return super.toString() + "M" + "x" + X_COORDINATE + "y" + Y_COORDINATE + "a" + ANGLE + "i" + PLAYER_ID;
    }

    @Override
    public void apply(GameThread gameThread) {
        gameThread.getPlayerArray()[PLAYER_ID].setCoordinates(X_COORDINATE, Y_COORDINATE);
        gameThread.getPlayerArray()[PLAYER_ID].setAngle(ANGLE);
    }
}
