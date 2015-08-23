package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Lukas on 21.08.2015.
 */
public class SlimeTrail extends Entity {
    private static final Bitmap SLIME_TRAIL_BITMAP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.slime_trail), Map.TILE_SIDE, Map.TILE_SIDE, false);
    private static double LIFETIME = Tick.TICK * 5;
    public static float TRAIL_RADIUS = 0.5f;

    public boolean removable = false;

    private final double TIME_CREATED;

    public SlimeTrail(float x, float y){
        super(x, y);
        TIME_CREATED = GameThread.getSynchronizedTick();
    }

    public void update(){
        if(TIME_CREATED + LIFETIME <= GameThread.getSynchronizedTick()){
            removable = true;
            Log.d("slime", "SlimeTrail removed");
        }

    }

    public void render(Canvas c){
        c.drawBitmap(SLIME_TRAIL_BITMAP,
                GameClient.getHalfScreenWidth() + (xCoordinate - GameThread.getUser().getXCoordinate()) * Map.TILE_SIDE - TRAIL_RADIUS * Map.TILE_SIDE,
                GameClient.getHalfScreenHeight() + (yCoordinate - GameThread.getUser().getYCoordinate()) * Map.TILE_SIDE - TRAIL_RADIUS * Map.TILE_SIDE,
                null);
    }


}
