package ch.imlee.maturarbeit.game.map;

import android.graphics.Bitmap;

public class Tile {

    // tells if it can be passed through by the Players
    public final boolean SOLID;
    // tells if the Player falls when standing only on this tile
    public final boolean FALL_THROUGH;
    public final Bitmap BMP;

    public Tile (Bitmap bmp, boolean solid, boolean fallThrough){
        SOLID = solid;
        BMP = bmp;
        FALL_THROUGH = fallThrough;
    }
}
