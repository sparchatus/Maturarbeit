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
    public final boolean FALL_THROUGH;
    public final Bitmap BMP;
    public Tile (Bitmap bmp, boolean solid, boolean fallThrough){
        SOLID = solid;
        BMP = bmp;
        FALL_THROUGH = fallThrough;
    }
}
