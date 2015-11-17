package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.views.GameSurface;

public class SlimeTrail extends Entity {
    private final Bitmap SLIME_TRAIL_BITMAP;
    private static final double LIFETIME = Tick.TICK * 5;
    public final float TRAIL_RADIUS;

    public boolean removable = false;

    private final double TIME_CREATED;

    public SlimeTrail(float x, float y, float radius){
        super(x, y);
        TRAIL_RADIUS = radius * Map.TILE_SIDE;
        SLIME_TRAIL_BITMAP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.slime_trail), (int)(2*TRAIL_RADIUS), (int)(2*TRAIL_RADIUS), false);
        TIME_CREATED = GameThread.getSynchronizedTick();
    }

    public void update(){
        if(TIME_CREATED + LIFETIME <= GameThread.getSynchronizedTick()){
            removable = true;
            Log.d("slime", "SlimeTrail removed");
        }

    }

    // the Objects are generally drawn in relation to the User position on the Map because the User's position on the screen is constant
    public void render(Canvas c){
        c.drawBitmap(SLIME_TRAIL_BITMAP,
                GameSurface.getSurfaceWidth() / 2+ (xCoordinate - GameThread.getUser().getXCoordinate()) * Map.TILE_SIDE - TRAIL_RADIUS * Map.TILE_SIDE,
                GameSurface.getSurfaceHeight() / 2 + (yCoordinate - GameThread.getUser().getYCoordinate()) * Map.TILE_SIDE - TRAIL_RADIUS * Map.TILE_SIDE,
                null);
    }


}
