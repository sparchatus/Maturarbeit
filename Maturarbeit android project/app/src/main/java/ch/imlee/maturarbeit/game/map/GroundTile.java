package ch.imlee.maturarbeit.game.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.activities.GameClient;
import ch.imlee.maturarbeit.views.GameSurface;

/**
 * Created by Lukas on 09.09.2015.
 */
public class GroundTile extends Tile {
    public GroundTile(){
        super(GROUND_TILE_BITMAP, false);
    }
}
