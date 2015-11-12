package ch.imlee.maturarbeit.game.Controller;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.game.map.Map;

/**
 * Created by Sandro on 09.09.2015.
 */
public class FluffyGameSurfaceController extends GameSurfaceController{

    private final float MAX_FOCUS_RANGE = 4.0f;
    private float lastPlayerRadius;

    private final Bitmap FOCUS_BMP;
    private Bitmap scaledFocusBmp;
    private static Player focusedPlayer;

    public FluffyGameSurfaceController(User user, int surfaceWidth, int surfaceHeight, Resources rec) {
        super(user, surfaceWidth, surfaceHeight);
        FOCUS_BMP = BitmapFactory.decodeResource(rec, R.drawable.focus_overlay);
        scaledFocusBmp = Bitmap.createScaledBitmap(FOCUS_BMP, Map.TILE_SIDE, Map.TILE_SIDE, false);
    }

    /**
     * This method is only used when the user is playing as Fluffy and it checks if the player who is focused is still in range.
     * Also it has to match the size of the overlay to the player size.
     */
    @Override
    public void update() {
        super.update();
        if (focusedPlayer!= null){
            if(Math.pow(focusedPlayer.getXCoordinate() - GameThread.getUser().getXCoordinate(), 2) + Math.pow(focusedPlayer.getYCoordinate() - GameThread.getUser().getYCoordinate(), 2) > MAX_FOCUS_RANGE) {
                focusedPlayer = null;
            }
            if (lastPlayerRadius != focusedPlayer.getPlayerRadius()) {
                lastPlayerRadius = focusedPlayer.getPlayerRadius();
                scaledFocusBmp = Bitmap.createScaledBitmap(FOCUS_BMP, (int) (lastPlayerRadius * Map.TILE_SIDE * 2), (int) (lastPlayerRadius * Map.TILE_SIDE * 2), false);
            }
        }
    }

    /**
     * Only the stun overlay has to be rendered here
     * @param canvas
     */
    @Override
    public void render(Canvas canvas) {
        if (focusedPlayer != null){
            canvas.drawBitmap(scaledFocusBmp, (float) ((focusedPlayer.getXCoordinate() - GameThread.getUser().getXCoordinate() - lastPlayerRadius) * Map.TILE_SIDE + halfSurfaceWidth), (float) ((focusedPlayer.getYCoordinate() - GameThread.getUser().getYCoordinate() - lastPlayerRadius) * Map.TILE_SIDE + halfSurfaceHeight), null);
        }
    }

    /**
     * In this system called method the it's checked whether the user has clicked an enemy player.
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("FluffyController", "onTouchEvent called");
        // it needs to be synchronized because two different threads are accessing the focusedPlayer variable, the playerArray and the playerCoordinates
        synchronized (GameThread.getHolder()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.i("FluffyController", "ACTION_DOWN Event");
                // it checks for every player if he is an enemy and in range.
                for (Player player : GameThread.getPlayerArray()) {
                    if (player.TEAM!=user.TEAM&&Math.pow(user.getXCoordinate() + (event.getX() - halfSurfaceWidth) / Map.TILE_SIDE - player.getXCoordinate(), 2) + Math.pow(user.getYCoordinate() + (event.getY() - halfSurfaceHeight) / Map.TILE_SIDE - player.getYCoordinate(), 2) <= Math.pow(player.getPlayerRadius(), 2)) {
                        controllerState = ControllerState.FOCUS;
                        focusedPlayer = player;
                        Log.i("FluffyController", "player focused");
                        // we don't have to follow this event further
                        return false;
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }
    public static Player getFocusedPlayer(){
        return focusedPlayer;
    }

    // to reset the focus when the skill is activated
    public static void nullFocusedPlayer(){
        Log.i("FluffyController", "player nulled");
        focusedPlayer = null;
    }
}
