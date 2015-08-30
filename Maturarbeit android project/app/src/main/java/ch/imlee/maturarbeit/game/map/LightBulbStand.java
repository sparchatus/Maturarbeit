package ch.imlee.maturarbeit.game.map;

import android.graphics.Bitmap;

/**
 * Created by Sandro on 27.08.2015.
 */
public class LightBulbStand extends Tile {
    public final byte TEAM;
    public final float CENTER_X, CENTER_Y;

    private boolean isFree;

    public LightBulbStand(int xCoordinate, int yCoordinate, Bitmap bmp, boolean solid, byte team) {
        super(bmp, solid);
        TEAM = team;
        CENTER_X = xCoordinate + 0.5f;
        CENTER_Y = yCoordinate + 0.5f;
        isFree = true;
    }

    public void setIsFree(boolean isFree){
        this.isFree = isFree;
    }

    public boolean isFree() {
        return isFree;
    }
}
