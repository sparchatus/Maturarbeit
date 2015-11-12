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

    private static boolean posChanged;

    protected static double halfSurfaceWidth, halfSurfaceHeight;

    /**
     * finger distance relative to the middle of the screen
     */
    private static double xFingerDistance, yFingerDistance;

    protected static User user;
    protected static ControllerState controllerState = ControllerState.NULL;

    public GameSurfaceController(User user, int surfaceWidth, int surfaceHeight) {
        this.user = user;
        halfSurfaceWidth = surfaceWidth / 2;
        halfSurfaceHeight = surfaceHeight / 2;
    }

    public void update(){
        synchronized (GameThread.getHolder()) {
            synchronized (controllerState) {
                if (controllerState == ControllerState.AIMING && posChanged) {
                    // some trigonometry to calculate the new user angle
                    double angle = Math.acos(xFingerDistance / Math.sqrt(Math.pow(xFingerDistance, 2) + Math.pow(yFingerDistance, 2)));
                    if (yFingerDistance <= 0) {
                        angle *= -1;
                    }
                    user.setAngle(angle);
                    setPosChanged(false);
                }
            }
        }
    }

    /**
     * Required for the subclass called FluffyGameSurfaceController so we can easily call it's render method in the game thread
     * @param canvas
     */
    public void render(Canvas canvas){

    }

    /**
     * This method decides if the event is
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event){
        synchronized (controllerState) {
            // when the player lifts the finger
            if (event.getAction() == MotionEvent.ACTION_UP) {
                controllerState = ControllerState.NULL;
                return false;
            }
            // when touching the game surface
            else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                controllerState = ControllerState.AIMING;
            }
            // the position relative to the middle of the screen
            xFingerDistance = event.getX() - halfSurfaceWidth;
            yFingerDistance = event.getY() - halfSurfaceHeight;
            // telling the user that his angle was changed.
            setPosChanged(true);
            return true;
        }
    }

    private static synchronized void setPosChanged(boolean changed){
        posChanged = changed;
    }
}