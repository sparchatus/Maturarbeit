package ch.imlee.maturarbeit.game.map;

import android.graphics.Bitmap;

/**
 * Created by Sandro on 27.08.2015.
 */
public class LightBulbStand extends Tile {
    public final byte TEAM;
    public final float CENTER_X, CENTER_Y;
    public final byte ID;

    private boolean isFree;

    public LightBulbStand(int xCoordinate, int yCoordinate, Bitmap bmp, byte team, byte id) {
        super(bmp, true, false);
        TEAM = team;
        CENTER_X = xCoordinate + 0.5f;
        CENTER_Y = yCoordinate + 0.5f;
        isFree = true;
        ID = id;
    }

    public LightBulbStand putLightBulbOn(){
        isFree = false;
        return this;
    }

    public LightBulbStand removeLightbulb(){
        isFree = true;
        return null;
    }

    public boolean isFree() {
        return isFree;
    }
}
