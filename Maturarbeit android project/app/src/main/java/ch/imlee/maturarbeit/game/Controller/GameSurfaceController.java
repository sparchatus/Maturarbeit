package ch.imlee.maturarbeit.game.Controller;

import android.view.MotionEvent;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.game.map.Map;

/**
 * Created by Sandro on 23.08.2015.
 */
public class GameSurfaceController {

    private static User user;
    private static double halfSurfaceWidth, halfSurfaceHeight;
    private static ControllerState controllerState = ControllerState.NULL;
    private static boolean posChanged;
    private static double xFingerDistance, yFingerDistance;

    public GameSurfaceController(User user, int surfaceWidth, int surfaceHeight) {
        this.user = user;
        halfSurfaceWidth = surfaceWidth / 2;
        halfSurfaceHeight = surfaceHeight / 2;
    }

    public void update(){
        synchronized (controllerState) {
            if (posChanged && controllerState == ControllerState.AIMING) {
                double angle = Math.acos(xFingerDistance / Math.sqrt(Math.pow(xFingerDistance, 2) + Math.pow(yFingerDistance, 2)));
                if (yFingerDistance <= 0) {
                    angle *= -1;
                }
                user.setAngle(angle);
                setPosChanged(false);
            }
        }
    }

    public static boolean onTouchEvent(MotionEvent event){
        synchronized (controllerState) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                for (Player player : GameThread.getPlayerArray()) {
                    if (Math.sqrt(Math.pow((user.getXCoordinate() + (event.getX() - halfSurfaceWidth) / Map.TILE_SIDE) - player.getXCoordinate(), 2) + Math.pow((user.getYCoordinate() + (event.getY() - halfSurfaceHeight) / Map.TILE_SIDE) - player.getYCoordinate(), 2)) < user.getPLAYER_RADIUS()) {
                        controllerState = ControllerState.FOCUS;
                        return false;
                    }
                }
                controllerState = ControllerState.AIMING;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                controllerState = ControllerState.NULL;
                return false;
            }
            xFingerDistance = event.getX() - halfSurfaceWidth;
            yFingerDistance = event.getY() - halfSurfaceHeight;
            setPosChanged(true);
            return true;
        }
    }

    private static synchronized void setPosChanged(boolean changed){
        posChanged = changed;
    }
}