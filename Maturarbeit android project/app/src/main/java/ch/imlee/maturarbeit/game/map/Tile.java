package ch.imlee.maturarbeit.game.map;

import android.graphics.Bitmap;

/**
 * Created by Sandro on 04.06.2015.
 */
public class Tile {
    public final boolean SOLID;
    private boolean slimy;
    public final Bitmap BMP;

    public Tile (Bitmap bmp, boolean solid){
        SOLID = solid;
        slimy = false;
        BMP = bmp;
    }
}
