package ch.imlee.maturarbeit.game.Controller;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Fluffy;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.game.map.Map;

/**
 * Created by Sandro on 09.09.2015.
 */
public class FluffyGameSurfaceController extends GameSurfaceController{

    private Fluffy fluffyUser;
    private Bitmap FOCUS_BMP;

    public FluffyGameSurfaceController(User user, int surfaceWidth, int surfaceHeight, Resources rec) {
        super(user, surfaceWidth, surfaceHeight);
        FOCUS_BMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.focus_overlay), Map.TILE_SIDE, Map.TILE_SIDE, false);
        fluffyUser = (Fluffy) user;
    }

    @Override
    public void update() {
        super.update();
        if (focusChanged){
            fluffyUser.setFocusedPlayer(focusedPlayer);
        }
    }

    @Override
    public void render(Canvas canvas) {
        if (focusedPlayer != null){
            canvas.drawBitmap(FOCUS_BMP, (float) (focusedPlayer.getXCoordinate() - GameThread.getUser().getXCoordinate() + halfSurfaceWidth - Map.TILE_SIDE / 2), (float) (focusedPlayer.getYCoordinate() - GameThread.getUser().getYCoordinate() + halfSurfaceHeight - Map.TILE_SIDE / 2), null);
        }
    }

    public static void focusedPlayerNull(){
        focusedPlayer = null;
    }
}
