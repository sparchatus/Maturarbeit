package ch.imlee.maturarbeit.game.Controller;

import android.graphics.Canvas;
import android.view.MotionEvent;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.game.map.Map;

/**
 * Created by Sandro on 23.08.2015.
 */
public class GameSurfaceController {

    protected static User user;
    protected static double halfSurfaceWidth, halfSurfaceHeight;
    protected static ControllerState controllerState = ControllerState.NULL;
    private static boolean posChanged;
    private static double xFingerDistance, yFingerDistance;

    public GameSurfaceController(User user, int surfaceWidth, int surfaceHeight) {
        this.user = user;
        halfSurfaceWidth = surfaceWidth / 2;
        halfSurfaceHeight = surfaceHeight / 2;
    }

    public void update(){
        synchronized (controllerState) {
            if (controllerState == ControllerState.AIMING && posChanged) {
                double angle = Math.acos(xFingerDistance / Math.sqrt(Math.pow(xFingerDistance, 2) + Math.pow(yFingerDistance, 2)));
                if (yFingerDistance <= 0) {
                    angle *= -1;
                }
                user.setAngle(angle);
                setPosChanged(false);
                if (user.getAngle() != angle){
                    user.angleHasChanged();
                }
            }
        }
    }

    public void render(Canvas canvas){

    }

    public boolean onTouchEvent(MotionEvent event){
        synchronized (controllerState) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                controllerState = ControllerState.NULL;
                return false;
            }else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                controllerState = ControllerState.AIMING;
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