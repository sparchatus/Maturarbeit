package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 05.06.2015.
 */
public class Player extends Entity implements Tick {

    protected final byte ID;
    public final byte TEAM;
    public final int TILE_SIDE;
    protected final int STUN_TIME = 3000 / Tick.TIME_PER_TICK;
    protected final int BAR_HEIGHT;
    protected final float PLAYER_RADIUS = 0.5f;

    protected boolean stunned;
    protected boolean invisible;
    protected boolean slimy;
    protected boolean dead;

    protected int reviveTick;

    protected double stunTick;

    protected double angle;

    protected User user;
    protected final Paint BAR_BACKGROUND_COLOR;
    protected final Paint STRENGTH_BAR_COLOR;
    protected final Bitmap PLAYER_BMP;
    protected final Bitmap STUN_BMP;

    public Player(float entityXCoordinate, float entityYCoordinate, PlayerType type, Map map, byte Team, byte playerId, User theUser) {
        super(entityXCoordinate, entityYCoordinate);
        TILE_SIDE = map.TILE_SIDE;
        if (type == PlayerType.FLUFFY){
            PLAYER_BMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.fluffy), TILE_SIDE, TILE_SIDE, false);
        }else if (type == PlayerType.SLIME){
            PLAYER_BMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.slime), TILE_SIDE, TILE_SIDE, false);
        }else if (type == PlayerType.GHOST){
            PLAYER_BMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.ghost), TILE_SIDE, TILE_SIDE, false);
        }else {
            PLAYER_BMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.mipmap.ic_launcher), TILE_SIDE, TILE_SIDE, false);
        }
        STUN_BMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.stun_overlay), TILE_SIDE, TILE_SIDE, false);
        TEAM = Team;
        BAR_HEIGHT = TILE_SIDE / 5;
        BAR_BACKGROUND_COLOR = new Paint();
        BAR_BACKGROUND_COLOR.setColor(0x50000000);
        STRENGTH_BAR_COLOR = new Paint();
        user = theUser;
        if (user == null || TEAM == user.TEAM){
            STRENGTH_BAR_COLOR.setColor(0xff00ff00);
        }else {
            STRENGTH_BAR_COLOR.setColor(0xffff0000);
        }
        stunned = false;
        invisible = false;
        this.ID = playerId;
    }

    public Canvas render(Canvas canvas){
        if (!invisible) {
            Matrix matrix = new Matrix();
            matrix.postRotate((float) (angle / 2 / Math.PI * 360) - 90);
            Bitmap rotated = Bitmap.createBitmap(PLAYER_BMP, 0, 0, PLAYER_BMP.getWidth(), PLAYER_BMP.getHeight(), matrix, true);
            canvas.drawBitmap(rotated, (xCoordinate - GameThread.getUser().getXCoordinate()) * TILE_SIDE + GameClient.getHalfScreenWidth() - rotated.getWidth() / 2, (yCoordinate - GameThread.getUser().getYCoordinate()) * TILE_SIDE + GameClient.getHalfScreenHeight() - rotated.getHeight() / 2, null);
        }
        if (stunned){
            canvas.drawBitmap(STUN_BMP, (xCoordinate - GameThread.getUser().getXCoordinate() - PLAYER_RADIUS) * TILE_SIDE + GameClient.getHalfScreenWidth(), (yCoordinate - GameThread.getUser().getYCoordinate() - PLAYER_RADIUS) * TILE_SIDE + GameClient.getHalfScreenHeight(), null);
        }
        return canvas;
    }

    public void update(){
        if (stunned && stunTick <= GameThread.getSynchronizedTick()){
            stunned = false;
        }
    }

    protected void death(){
        //todo:loose flag
        setCoordinates(Map.getStartX(TEAM), Map.getStartY(TEAM));
        dead = true;
        if (reviveTick <= GameThread.getSynchronizedTick());
    }

    public void stun(double stunTick){
        stunned = true;
        this.stunTick = stunTick;
    }

    public void setInvisible(boolean invisible){
        this.invisible = invisible;
    }

    public boolean getInvisible(){
        return invisible;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle){
        this.angle = angle;
    }

    public byte getID(){
        return ID;
    }

    public void setSlimy(boolean slimy){
        this.slimy = slimy;
    }

    public byte getTeam(){
        return TEAM;
    }
}