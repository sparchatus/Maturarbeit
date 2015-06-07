package ch.imlee.maturarbeit.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 05.06.2015.
 */
public class Player extends Entity implements Tick, Constants{

    protected final int TEAM;
    protected final int PLAYER_SIDE;
    protected final int BAR_HEIGHT;

    protected boolean stunned;

    protected int stunTick;

    protected double angle;

    protected User user;
    protected final Paint BAR_BACKGROUND_COLOR;
    protected final Paint STRENGTH_BAR_COLOR;
    protected final Bitmap PLAYER_BMP;
    protected final Bitmap STUN_BMP;

    public Player(float entityXCoordinate, float entityYCoordinate, PlayerType type, Map map, GameSurface.GameThread gameThread, int Team) {
        super(entityXCoordinate, entityYCoordinate, gameThread);
        if (type == PlayerType.FLUFFY){
            PLAYER_BMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameClient.getRec(), R.drawable.fluffy), map.TILE_SIDE, map.TILE_SIDE, false);
        }else if (type == PlayerType.SLIME){
            PLAYER_BMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameClient.getRec(), R.drawable.slime), map.TILE_SIDE, map.TILE_SIDE, false);
        }else if (type == PlayerType.GHOST){
            PLAYER_BMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameClient.getRec(), R.drawable.ghost), map.TILE_SIDE, map.TILE_SIDE, false);
        }else {
            PLAYER_BMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameClient.getRec(), R.mipmap.ic_launcher), map.TILE_SIDE, map.TILE_SIDE, false);
        }
        STUN_BMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameClient.getRec(), R.drawable.stun_overlay), map.TILE_SIDE, map.TILE_SIDE, false);
        PLAYER_SIDE = map.TILE_SIDE;
        TEAM = Team;
        BAR_HEIGHT = PLAYER_SIDE / 5;
        STRENGTH_BAR_COLOR = new Paint();
        if (TEAM == user.TEAM) {
            STRENGTH_BAR_COLOR.setColor(0xff00ff00);
        }else  {
            STRENGTH_BAR_COLOR.setColor(0xffff0000);
        }
        BAR_BACKGROUND_COLOR = new Paint();
        BAR_BACKGROUND_COLOR.setColor(0x50000000);
    }

    public Canvas render(Canvas canvas){
        Matrix matrix = new Matrix();
        matrix.postRotate((float) (angle / 2 / Math.PI * 360) - 90);
        Bitmap rotated = Bitmap.createBitmap(PLAYER_BMP, 0, 0, PLAYER_BMP.getWidth(), PLAYER_BMP.getHeight(), matrix, true);
        canvas.drawBitmap(rotated, (xCoordinate - user.getXCoordinate()) * PLAYER_SIDE + GameClient.getHalveScreenWidth() - rotated.getWidth() / 2, (yCoordinate - user.getYCoordinate()) * PLAYER_SIDE + GameClient.getHalveScreenHeight() - rotated.getHeight() / 2, null);
        if (stunned){
            canvas.drawBitmap(STUN_BMP, (xCoordinate - user.getXCoordinate() - PLAYER_SIDE / 2) * PLAYER_SIDE + GameClient.getHalveScreenWidth(), (yCoordinate - user.getYCoordinate() - PLAYER_SIDE / 2) * PLAYER_SIDE + GameClient.getHalveScreenHeight(), null);
        }
        return canvas;
    }

    public void update(){
        if (stunned && stunTick <= gameThread.getSynchronizedTick()){
            stunned = false;
        }
    }

    public void setAngle(double playerAngle){
        angle = playerAngle;
    }

    public void setXCoordinate(float playerXCoordinate){
        xCoordinate = playerXCoordinate;
    }

    public void setYCoordinate(float playerYCoordinate){
        yCoordinate = playerYCoordinate;
    }

    public void setCoordinates(float playerXCoordinate, float playerYCoordinate){
        xCoordinate = playerXCoordinate;
        yCoordinate = playerYCoordinate;
    }

    public void stun(){
        stunned = true;
        stunTick = (int) (gameThread.getSynchronizedTick() + STUN_TIME);
    }
}
