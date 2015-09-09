package ch.imlee.maturarbeit.game.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.views.GameSurface;

/**
 * Created by Lukas on 09.09.2015.
 */
public class WallTile extends Tile {
    public WallTile(){
        super(WALL_TILE_BITMAP, true);
    }
}
