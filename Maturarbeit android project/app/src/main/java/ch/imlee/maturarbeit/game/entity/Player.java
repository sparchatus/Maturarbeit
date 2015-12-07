package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Sound.StunSound;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.views.GameSurface;

public class Player extends Entity implements Tick {

    protected boolean stunned = false;
    protected boolean invisible = false;
    protected boolean slimy;
    protected boolean dead;

    protected final byte ID;
    public final byte TEAM;

    protected final int STUN_TIME = 3000 / Tick.TIME_PER_TICK;
    protected final int BAR_HEIGHT;
    protected final int MAX_STRENGTH = 100;
    protected final int SLIME_EJECTION_RATE = Tick.TICK / 5;
    protected static float halfGameSurfaceWidth, halfGameSurfaceHeight;
    protected int strength;

    public static final float START_RADIUS = 0.4f;
    protected float playerRadius = START_RADIUS;

    protected double lastSlimeEjection = 0;
    protected double stunTick;
    protected double angle;

    public final String NAME;

    protected final Paint BAR_BACKGROUND_COLOR;
    protected final Paint STRENGTH_BAR_COLOR;
    protected final Bitmap STUN_BMP;
    protected final PlayerType TYPE;
    protected LightBulb lightBulb;
    protected User user;
    protected Bitmap PLAYER_BMP;
    protected Bitmap scaledPlayerBmp;
    protected  Bitmap scaledStunBmp;

    public Player(PlayerType type, Map map, byte team, byte playerId, String name) {
        super(map.getStartX(team), map.getStartY(team));
        TYPE = type;
        //setting the Player Bitmap according to the PlayerType
        if (type == PlayerType.FLUFFY){
            PLAYER_BMP = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.fluffy);
        }else if (type == PlayerType.SLIME){
            PLAYER_BMP = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.slime);
        }else if (type == PlayerType.GHOST){
            PLAYER_BMP = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.ghost);
        }else {
            PLAYER_BMP = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.ic_launcher);
        }
        scaledPlayerBmp = Bitmap.createScaledBitmap(PLAYER_BMP, (int) (playerRadius * 2 * Map.TILE_SIDE), (int) (playerRadius * 2 * Map.TILE_SIDE), false);
        STUN_BMP = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.stun_overlay);
        scaledStunBmp = Bitmap.createScaledBitmap(STUN_BMP, (int) (playerRadius * 2 * Map.TILE_SIDE),(int) (playerRadius * 2 * Map.TILE_SIDE), false);
        TEAM = team;
        BAR_HEIGHT = Map.TILE_SIDE / 4;
        BAR_BACKGROUND_COLOR = new Paint();
        BAR_BACKGROUND_COLOR.setColor(0x50000000);
        STRENGTH_BAR_COLOR = new Paint();
        user = GameThread.getUser();
        if (user == null || TEAM == user.TEAM){
            STRENGTH_BAR_COLOR.setColor(0xff00ff00);
        }else {
            STRENGTH_BAR_COLOR.setColor(0xffff0000);
        }
        this.ID = playerId;
        NAME = name;
        halfGameSurfaceWidth = GameSurface.getSurfaceWidth() / 2f;
        halfGameSurfaceHeight = GameSurface.getSurfaceHeight() / 2f;
    }


    public void update(){
        // the PLayer removes the stun at the right time
        if (stunned && stunTick <= GameThread.getSynchronizedTick()) {
            stunned = false;
        }
        // if he is of the PlayerType Slime and has his skill activated, he ejects a SlimeTrail
        if(slimy) {
            if (GameThread.getSynchronizedTick() - SLIME_EJECTION_RATE >= lastSlimeEjection) {
                lastSlimeEjection = GameThread.getSynchronizedTick();
                SlimeTrail slimeTrail = new SlimeTrail(getXCoordinate(), getYCoordinate(), getPlayerRadius());
                GameThread.addSlimeTrail(slimeTrail);
            }
        }
    }

    // the Objects are generally drawn in relation to the User position on the Map because the User's position on the screen is constant
    public void render(Canvas canvas){
        //render coordinates rx and ry are just used to better understand the code and shorten the code lines.
        float rx, ry;
        // if the Player isn't invisible nor dead he is rendered
        if (!invisible && !dead) {
            // rotating the Player Bitmap before rendering it.
            Matrix matrix = new Matrix();
            matrix.postRotate((float) (angle / 2 / Math.PI * 360) - 90);
            Bitmap rotated = Bitmap.createBitmap(scaledPlayerBmp, 0, 0, scaledPlayerBmp.getWidth(), scaledPlayerBmp.getHeight(), matrix, false);
            // for better overview the coordinates are first calculated and then used in a later step
            rx = (xCoordinate - GameThread.getUser().getXCoordinate()) * Map.TILE_SIDE + halfGameSurfaceWidth - rotated.getWidth() / 2f;
            ry = (yCoordinate - GameThread.getUser().getYCoordinate()) * Map.TILE_SIDE + halfGameSurfaceHeight - rotated.getHeight() / 2f;
            //this is the actual rendering
            canvas.drawBitmap(rotated, rx, ry, null);        }
        // if the Player is stunned he gets an overlay
        if (stunned){
            rx = (xCoordinate - GameThread.getUser().getXCoordinate() - playerRadius) * Map.TILE_SIDE + halfGameSurfaceWidth;
            ry = (yCoordinate - GameThread.getUser().getYCoordinate() - playerRadius) * Map.TILE_SIDE + halfGameSurfaceHeight;
            canvas.drawBitmap(scaledStunBmp, rx, ry, null);
        }
        // if the Player is in possession of a LightBulb, he renders his strength bar below himself
        if (lightBulb != null) {
            rx = (xCoordinate - GameThread.getUser().getXCoordinate() - playerRadius) * Map.TILE_SIDE + halfGameSurfaceWidth;
            ry = (yCoordinate - GameThread.getUser().getYCoordinate() + playerRadius) * Map.TILE_SIDE + halfGameSurfaceHeight;
            canvas.drawRect(rx,ry,rx + 2 * playerRadius * Map.TILE_SIDE,ry + BAR_HEIGHT, BAR_BACKGROUND_COLOR);
            canvas.drawRect(rx,ry,rx + 2 * playerRadius * strength / MAX_STRENGTH * Map.TILE_SIDE,ry + BAR_HEIGHT, STRENGTH_BAR_COLOR);
        }
    }

    // getting stunned activates stunned, plays a sound and activates the stun timer
    public void stun(double stunTick){
        new StunSound().start(TIME_PER_TICK * STUN_TIME);
        stunned = true;
        this.stunTick = stunTick;
    }

    public void particleHit(){
        strength -= 10;
        if (strength < 0){
            strength = 0;
        }
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

    public PlayerType getType(){
        return TYPE;
    }

    public void bulbReceived(int bulbID){
        strength = MAX_STRENGTH;
        lightBulb = GameThread.getLightBulbArray()[bulbID];
        lightBulb.pickUp(this);
    }

    public void bulbLost(){
        lightBulb.fallOnFloor();
        lightBulb = null;
    }

    public void putBulbOnStand(byte lightBulbStandID){
        lightBulb.putOnLightBulbStand(Map.getFriendlyLightBulbStands(TEAM)[lightBulbStandID]);
        lightBulb = null;
    }

    public void setDead(boolean dead){
        this.dead = dead;
    }

    public void setPlayerRadius(float radius){
        if(radius < 0.01f) radius = 0.01f;
        playerRadius = radius;
        int side = (int) (playerRadius * 2 * Map.TILE_SIDE);
        scaledPlayerBmp = Bitmap.createScaledBitmap(PLAYER_BMP, side, side, false);
        scaledStunBmp = Bitmap.createScaledBitmap(STUN_BMP, side, side, false);
    }

    public boolean getDead(){
        return dead;
    }

    public float getPlayerRadius(){
        return playerRadius;
    }
}