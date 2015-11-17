package ch.imlee.maturarbeit.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.special_screens.EndGameScreen;

public class JoystickSurface extends SurfaceView implements SurfaceHolder.Callback {

    private static int halfJoystickWidth;
    private static JoystickController joystickController;

    private SurfaceHolder holder;

    public JoystickSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        joystickController = new JoystickController();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // needs to be called to get the real width and height
        invalidate();
        halfJoystickWidth = getWidth()/2;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // needs to be called to get the real width and height
        invalidate();
        this.halfJoystickWidth = getWidth()/2;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (GameThread.getEndGameActive()){
            return EndGameScreen.onTouch(event);
        }
        // if the GameThread wasn't done loading yet it would cause an error because there would be no User
        if (GameThread.getLoading()) {
            return false;
        }
        return joystickController.onTouch(event);
    }

    public static void update() {
        joystickController.update();
    }

    public static Canvas render(Canvas canvas){
        return joystickController.render(canvas);
    }

    private class JoystickController {
        private boolean moving;
        private boolean posChanged;

        private int joystickRadius;

        // position of the joystick, this is set every time the user puts his finger down on the joystick surface afresh
        private float xJoystickCoordinate, yJoystickCoordinate;
        //distance of the finger to the joystick coordinate
        private float xFingerDistance, yFingerDistance;
        // position of the middle circle of the joystick
        private float xMiddleCoordinate, yMiddleCoordinate;

        // distance of the finger to the joystick coordinate
        private double radiusDistance;

        // the middle variable is also used to synchronize the onTouch call and the updates
        private Bitmap ring, middle;

        public JoystickController() {
            joystickRadius = halfJoystickWidth;
            middle = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.joystick_button_middle), joystickRadius * 2, joystickRadius * 2, false);
            ring = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.joystick_button_ring), joystickRadius * 2, joystickRadius * 2, false);
        }

        public void update() {
            synchronized (joystickController) {
                // if the joystick isn't active (no finger on the screen inside the joystick surface) no further calculations are necessary
                if (!moving) {
                    GameThread.getUser().setVelocity(0);
                    return;
                }
                // if the position of the finger relative to the joystick has changed
                if (posChanged) {
                    // as in User described, with velocity the desired speed is meant
                    // first the distance of the finger to the centre of the joystick is calculated
                    radiusDistance = Math.sqrt(Math.pow(xFingerDistance, 2) + Math.pow(yFingerDistance, 2));
                    // now there are 2 different cases: 1
                    // 1. the finger is inside the maximum range -> the velocity is proportional to the joystickRadius and the middle button position f the joystick is the finger position
                    // 2. the finger is outside and the velocity is maximal thus no need t calculate it but the position of the middle button has to be calculated because of the maximal joystick distance
                    if (radiusDistance <= joystickRadius) {
                        GameThread.getUser().setVelocity((float) (radiusDistance / joystickRadius));
                        xMiddleCoordinate = xFingerDistance;
                        yMiddleCoordinate = yFingerDistance;
                    } else {
                        GameThread.getUser().setVelocity(1);
                        xMiddleCoordinate = (float) (xFingerDistance / radiusDistance * joystickRadius);
                        yMiddleCoordinate = (float) (yFingerDistance / radiusDistance * joystickRadius);
                    }
                    // it is necessary to exclude the case that the user hits exactly the middle of the joystick because this would cause a mathematical error
                    if (radiusDistance != 0) {
                        double angle = Math.acos(xFingerDistance / radiusDistance);
                        if (yFingerDistance <= 0) {
                            angle *= -1;
                        }
                        GameThread.getUser().setAngle(angle);
                    }
                    // reset the variable
                    posChanged = false;
                }
            }
        }

        // the joystick is only visible when in use (when moving is true)
        // rendering the outer ring of the joystick at the joystick coordinates
        // rendering the middle circle  at the earlier calculate position
        public Canvas render(Canvas canvas) {
            synchronized (joystickController) {
                if (moving) {
                    canvas.drawBitmap(ring, xJoystickCoordinate - joystickRadius, yJoystickCoordinate - joystickRadius, null);
                    canvas.drawBitmap(middle, xJoystickCoordinate - joystickRadius + xMiddleCoordinate, yJoystickCoordinate - joystickRadius + yMiddleCoordinate, null);
                }
            }
            return canvas;
        }

        public boolean onTouch(MotionEvent event) {
            synchronized (joystickController) {
                // if the user removes the finger, the joystick disappears (moving = false)
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    moving = false;
                    return false;
                }
                //when the user puts his finger down, the coordinates of the joystick have to be set
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xJoystickCoordinate = event.getX();
                    yJoystickCoordinate = event.getY();
                    moving = true;
                }
                //this is the default case. when it is not a down nor an up event the fingerDistance is updated
                xFingerDistance = event.getX() - xJoystickCoordinate;
                yFingerDistance = event.getY() - yJoystickCoordinate;
                // tell the update method tht something has changed
                posChanged = true;
                // this event has to be traced further such that user finger movements are correctly registered
                return true;
            }
        }

    }
}
