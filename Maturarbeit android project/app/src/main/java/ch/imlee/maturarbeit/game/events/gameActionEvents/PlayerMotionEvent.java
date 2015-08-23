package ch.imlee.maturarbeit.game.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.User;

/**
 * Created by Sandro on 15.06.2015.
 */
public class PlayerMotionEvent extends GameActionEvent {

    private final float X_COORDINATE, Y_COORDINATE;
    private final double ANGLE;


    public PlayerMotionEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        X_COORDINATE = Float.valueOf(eventString.substring(eventString.indexOf("x") + 1, eventString.indexOf("y")));
        Y_COORDINATE = Float.valueOf(eventString.substring(eventString.indexOf("y") + 1, eventString.indexOf("a")));
        ANGLE = Double.valueOf(eventString.substring(eventString.indexOf("a") + 1, eventString.indexOf("i")));
    }

    public PlayerMotionEvent(User user){
        super(user.getID());
        X_COORDINATE = user.getXCoordinate();
        Y_COORDINATE = user.getYCoordinate();
        ANGLE = user.getAngle();
        senderID = user.getID();
    }

    @Override
    public String toString() {
        return super.toString() + "M" + "x" + X_COORDINATE + "y" + Y_COORDINATE + "a" + ANGLE + "i" + senderID;
    }

    @Override
    public void apply() {
        GameThread.getPlayerArray()[senderID].setCoordinates(X_COORDINATE, Y_COORDINATE);
        GameThread.getPlayerArray()[senderID].setAngle(ANGLE);
    }
}
