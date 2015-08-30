package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.views.GameSurface;

/**
 * Created by Lukas on 24.08.2015.
 */
public class Sweet extends Entity {
    private static final Bitmap SWEET_BITMAP = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.sweet), Map.TILE_SIDE, Map.TILE_SIDE, false);
    private final int ID;
    public Sweet(int x, int y, int id){
        super(x + 0.5f, y + 0.5f);
        ID = id;
    }
    public void render(Canvas canvas){
        canvas.drawBitmap(SWEET_BITMAP, GameSurface.getSurfaceWidth()/2+(this.xCoordinate-GameThread.getUser().xCoordinate - 0.5f)*Map.TILE_SIDE,
                GameSurface.getSurfaceHeight()/2+(this.yCoordinate-GameThread.getUser().yCoordinate - 0.5f)*Map.TILE_SIDE, null);
    }
    public int getID(){
        return ID;
    }
}
