package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Lukas on 21.08.2015.
 */
public class SlimeTrail extends Entity {
    private static final Bitmap SLIMETRAIL_BITMAP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.slime),
            (int)(GameThread.getUser().TILE_SIDE), (int)(GameThread.getUser().TILE_SIDE), false);
    private static double LIFETIME = Tick.TICK * 5;

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
        c.drawBitmap(SLIMETRAIL_BITMAP,
                GameClient.getHalfScreenWidth() + (xCoordinate - GameThread.getUser().getXCoordinate()) * GameThread.getUser().TILE_SIDE - SLIMETRAIL_BITMAP.getWidth()/2,
                GameClient.getHalfScreenHeight() + (yCoordinate - GameThread.getUser().getYCoordinate()) * GameThread.getUser().TILE_SIDE - SLIMETRAIL_BITMAP.getHeight()/2,
                null);
        Log.v("slime", "slime rendered at " + xCoordinate + " / " + yCoordinate + "BitmapSize = " + SLIMETRAIL_BITMAP.getWidth() + " * " + SLIMETRAIL_BITMAP.getHeight());
        Log.v("slime", "position on screen: " + GameClient.getHalfScreenWidth() + (((int) GameThread.getUser().getXCoordinate()) + xCoordinate - GameThread.getUser().getXCoordinate()) * GameThread.getUser().TILE_SIDE + " / " + GameClient.getHalfScreenHeight() + (((int) GameThread.getUser().getYCoordinate()) + yCoordinate - GameThread.getUser().getYCoordinate()) * GameThread.getUser().TILE_SIDE);
    }


}
