package ch.imlee.maturarbeit.game.Controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.views.GameSurface;

/**
 * Created by Sandro on 23.08.2015.
 */
public class JoystickController{

    private static boolean moving;
    private static boolean posChanged;

    private static int halfJoystickWidth, halfJoystickHeight;
    private static int joystickRadius;

    // position of the joystick, this is set every time the user puts his finger down on the joystick surface afresh
    private static float xJoystickCoordinate, yJoystickCoordinate;
    //distance of the finger to the joystick coordinate
    private static float xFingerDistance, yFingerDistance;
    // position of the middle circle of the joystick
    private float xMiddleCoordinate, yMiddleCoordinate;

    // distance of the finger to the joystick coordinate
    private static double radiusDistance;

    private static User user;
    // the middle variable is also used to synchronize the onTouch call and the updates
    private static Bitmap ring, middle;

    public JoystickController(User user, int surfaceWidth, int surfaceHeight){
        this.user = user;
        halfJoystickWidth = surfaceWidth / 2;
        halfJoystickHeight = surfaceHeight / 2;
        joystickRadius = halfJoystickWidth / 3 * 2;
        middle = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.joystick_button_middle), joystickRadius * 2, joystickRadius * 2, false);
        ring = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.joystick_button_ring), joystickRadius * 2, joystickRadius * 2, false);
    }

    public void update(){
        // if the joystick isn't active (no finger on the screen inside the joystick surface) no further calculations are necessary
        if (!moving){
            user.setVelocity(0);
            return;
        }
        // if the position of the finger relative to the joystick has changed
        if (posChanged) {
            synchronized (middle) {
                // as in User described, with velocity the desired speed is meant
                // first the distance of the finger to the centre of the joystick is calculated
                radiusDistance = Math.sqrt(Math.pow(xFingerDistance, 2) + Math.pow(yFingerDistance, 2));
                // now there are 2 different cases: 1
                // 1. the finger is inside the maximum range -> the velocity is proportional to the joystickRadius and the middle button position f the joystick is the finger position
                // 2. the finger is outside and the velocity is maximal thus no need t calculate it but the position of the middle button has to be calculated because of the maximal joystick distance
                if (radiusDistance <= joystickRadius) {
                    user.setVelocity((float) (radiusDistance / joystickRadius));
                    xMiddleCoordinate = xFingerDistance;
                    yMiddleCoordinate = yFingerDistance;
                } else {
                    user.setVelocity(1);
                    xMiddleCoordinate = (float) (xFingerDistance / radiusDistance * joystickRadius);
                    yMiddleCoordinate = (float) (yFingerDistance / radiusDistance * joystickRadius);
                }
                // it is necessary to exclude the case that the user hits exactly the middle of the joystick because this would cause a mathematical error
                if (radiusDistance != 0) {
                    double angle = Math.acos(xFingerDistance / radiusDistance);
                    if (yFingerDistance <= 0) {
                        angle *= -1;
                    }
                    user.setAngle(angle);
                }
                // reset the variable
                setPosChanged(false);
            }
        }
    }

    // the joystick is only visible when in use (when moving is true)
    // rendering the outer ring of the joystick at the joystick coordinates
    // rendering the middle circle  at the earlier calculate position
    public void render(Canvas canvas){
        synchronized (middle) {
            if (moving) {
                canvas.drawBitmap(ring, xJoystickCoordinate - joystickRadius, yJoystickCoordinate - joystickRadius, null);
                canvas.drawBitmap(middle, xJoystickCoordinate - joystickRadius + xMiddleCoordinate, yJoystickCoordinate - joystickRadius + yMiddleCoordinate, null);
            }
        }
    }

    public static boolean onTouchEvent(MotionEvent event){
        synchronized (middle) {
            // if the user removes the finger, the joystick disappears (moving = false)
            if (event.getAction() == MotionEvent.ACTION_UP) {
                moving = false;
                return false;
            }
            //when the user puts his finger down, the coordinates of the joystick have to be set
            else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                xJoystickCoordinate = event.getX();
                yJoystickCoordinate = event.getY();
            }
            moving = true;
            //this is the default case. when it is not a down nor an up event the fingerDistance is updated
            xFingerDistance = event.getX() - xJoystickCoordinate;
            yFingerDistance = event.getY() - yJoystickCoordinate;
        }
        // tell the update method tht something has changed
        setPosChanged(true);
        // this event has to be traced further such that user finger movements are correctly registered
        return true;
    }

    private synchronized static void setPosChanged(boolean changed){
        posChanged = changed;
    }
}