package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

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
            GameClient.getScreenHeight()/10, GameClient.getScreenHeight()/10, false);
    private static double LIFETIME = Tick.TICK * 5;

    private final double TIME_CREATED;
    public SlimeTrail(float x, float y){
        super(x, y);
        TIME_CREATED = GameThread.getSynchronizedTick();
    }

    public void update(){

    }

    public void render(Canvas c){
        c.drawBitmap(SLIMETRAIL_BITMAP,
                GameClient.getHalfScreenWidth() + (((int) GameThread.getUser().getXCoordinate()) + xCoordinate - GameThread.getUser().getXCoordinate()) * GameThread.getUser().TILE_SIDE,
                GameClient.getHalfScreenHeight() + (((int) GameThread.getUser().getYCoordinate()) + yCoordinate - GameThread.getUser().getYCoordinate())* GameThread.getUser().TILE_SIDE,
                null);
    }


}
