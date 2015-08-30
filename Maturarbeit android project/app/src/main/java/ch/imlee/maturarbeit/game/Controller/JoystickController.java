package ch.imlee.maturarbeit.game.Controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.views.GameSurface;

/**
 * Created by Sandro on 23.08.2015.
 */
public class JoystickController{
    private static User user;
    private static float xJoystickCoordinate, yJoystickCoordinate;
    private static float xFingerDistance, yFingerDistance;
    private static double radiusDistance;
    private float xMiddleCoordinate, yMiddleCoordinate;
    private static int halfJoystickWidth, halfJoystickHeight;
    private static int joystickRadius;
    private static boolean moving;
    private static Bitmap ring, middle;
    private static boolean posChanged;

    public JoystickController(User user, int surfaceWidth, int surfaceHeight){
        this.user = user;
        halfJoystickWidth = surfaceWidth / 2;
        halfJoystickHeight = surfaceHeight / 2;
        joystickRadius = halfJoystickWidth / 3 * 2;
        Log.i("joystickRadius", String.valueOf(joystickRadius));
        middle = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.joystick_button_middle), joystickRadius * 2, joystickRadius * 2, false);
        ring = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.joystick_button_ring), joystickRadius * 2, joystickRadius * 2, false);
    }

    public void update(){
        if (!moving){
            user.setVelocity(0);
            return;
        }
        if (posChanged) {
            synchronized (middle) {
                //as in @see #User described, with velocity the desired speed is meant
                radiusDistance = Math.sqrt(Math.pow(xFingerDistance, 2) + Math.pow(yFingerDistance, 2));
                if (radiusDistance <= joystickRadius) {
                    user.setVelocity((float) (radiusDistance / joystickRadius));
                    xMiddleCoordinate = xFingerDistance;
                    yMiddleCoordinate = yFingerDistance;
                } else {
                    user.setVelocity(1);
                    xMiddleCoordinate = (float) (xFingerDistance / radiusDistance * joystickRadius);
                    yMiddleCoordinate = (float) (yFingerDistance / radiusDistance * joystickRadius);
                }
                if (radiusDistance != 0) {
                    double angle = Math.acos(xFingerDistance / radiusDistance);
                    if (yFingerDistance <= 0) {
                        angle *= -1;
                    }
                    user.setAngle(angle);
                    if (user.getAngle() != angle){
                        user.angleHasChanged();
                    }
                }
                setPosChanged(false);
            }
        }
    }

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
            if (event.getAction() == MotionEvent.ACTION_UP) {
                moving = false;
                xFingerDistance = 0;
                yFingerDistance = 0;
                return false;
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                xJoystickCoordinate = event.getX();
                yJoystickCoordinate = event.getY();
            }
            moving = true;
            xFingerDistance = event.getX() - xJoystickCoordinate;
            Log.d("JoystickC onTouch x", String.valueOf(xFingerDistance));
            yFingerDistance = event.getY() - yJoystickCoordinate;
            Log.d("JoystickC onTouch y", String.valueOf(yFingerDistance));
        }
        setPosChanged(true);
        return true;
    }

    private synchronized static void setPosChanged(boolean changed){
        posChanged = changed;
    }
}