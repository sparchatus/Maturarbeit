package ch.imlee.maturarbeit.game.Controller;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

    private final Bitmap FOCUS_BMP;
    private Bitmap scaledFocusBmp;
    private Player focusedPlayer;

    public FluffyGameSurfaceController(User user, int surfaceWidth, int surfaceHeight, Resources rec) {
        super(user, surfaceWidth, surfaceHeight);
        FOCUS_BMP = BitmapFactory.decodeResource(rec, R.drawable.focus_overlay);
        scaledFocusBmp = Bitmap.createScaledBitmap(FOCUS_BMP, Map.TILE_SIDE, Map.TILE_SIDE, false);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(Canvas canvas) {
        if (focusedPlayer != null){
            scaledFocusBmp = Bitmap.createScaledBitmap(FOCUS_BMP, (int) (focusedPlayer.getPlayerRadius() * Map.TILE_SIDE * 2), (int) (focusedPlayer.getPlayerRadius() * Map.TILE_SIDE * 2), false);
            canvas.drawBitmap(scaledFocusBmp, (float) (focusedPlayer.getXCoordinate() - GameThread.getUser().getXCoordinate() + halfSurfaceWidth - Map.TILE_SIDE / 2), (float) (focusedPlayer.getYCoordinate() - GameThread.getUser().getYCoordinate() + halfSurfaceHeight - Map.TILE_SIDE / 2), null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (controllerState) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                for (Player player : GameThread.getPlayerArray()) {
                    if (Math.pow((user.getXCoordinate() + (event.getX() - halfSurfaceWidth) / Map.TILE_SIDE) - player.getXCoordinate(), 2) + Math.pow((user.getYCoordinate() + (event.getY() - halfSurfaceHeight) / Map.TILE_SIDE) - player.getYCoordinate(), 2) < Math.pow(user.getPlayerRadius(), 2)) {
                        controllerState = ControllerState.FOCUS;
                        return false;
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
