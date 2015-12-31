package ch.imlee.maturarbeit.game.map;

import android.graphics.Bitmap;

public class LightBulbStand extends Tile {

    public final byte TEAM;
    public final float CENTER_X, CENTER_Y;

    // a unique identifier
    public final byte ID;

    // tells if a LightBulb can be put on this LightBulbStand, respectively not if a LightBulb is already on this LightBulbStand
    private boolean isFree;

    public LightBulbStand(int xCoordinate, int yCoordinate, Bitmap bmp, byte team, byte id) {
        super(bmp, true, false);
        TEAM = team;
        // the position the LightBulb moves to when put on this LightBulbStand
        CENTER_X = xCoordinate + 0.5f;
        CENTER_Y = yCoordinate + 0.5f;

        // LightBulbStands are initialized without a LightBulb
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
