package ch.imlee.maturarbeit.game.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.views.GameSurface;

/**
 * Created by Sandro on 04.06.2015.
 */
public class Tile {
    public final boolean SOLID;
    public final Bitmap BMP;
    protected static final Bitmap BLUE_BASE_TILE_BITMAP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.blue_base_tile), Map.TILE_SIDE, Map.TILE_SIDE, false);
    protected static final Bitmap GREEN_BASE_TILE_BITMAP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.green_base_tile), Map.TILE_SIDE, Map.TILE_SIDE, false);
    protected static final Bitmap GROUND_TILE_BITMAP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.ground_tile), Map.TILE_SIDE, Map.TILE_SIDE, false);
    protected static final Bitmap SPAWN_TILE_BITMAP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.spawn_tile), Map.TILE_SIDE, Map.TILE_SIDE, false);
    protected static final Bitmap VOID_TILE_BITMAP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.void_tile), Map.TILE_SIDE, Map.TILE_SIDE, false);
    protected static final Bitmap WALL_TILE_BITMAP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.wall_tile), Map.TILE_SIDE, Map.TILE_SIDE, false);

    public Tile (Bitmap bmp, boolean solid){
        SOLID = solid;
        BMP = bmp;
    }
}
