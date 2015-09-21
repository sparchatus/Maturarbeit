package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.events.gameActionEvents.DeathEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.LightBulbEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.RadiusChangedEvent;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Sound.SlimeSound;
import ch.imlee.maturarbeit.game.Sound.StunSound;
import ch.imlee.maturarbeit.game.map.LightBulbStand;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.views.GameSurface;

/**
 * Created by Sandro on 05.06.2015.
 */
public class Player extends Entity implements Tick {

    protected final byte ID;
    public final byte TEAM;
    protected final int STUN_TIME = 3000 / Tick.TIME_PER_TICK;
    protected final int BAR_HEIGHT;
    public static final float startRadius = 0.4f;
    protected float playerRadius;
    protected final int MAX_STRENGTH = 100;
    private final int DEATH_TIME = 5 * Tick.TICK;

    protected boolean stunned;
    protected boolean invisible;
    protected boolean slimy;
    protected boolean dead;
    protected boolean flagPossessed = false;
    protected LightBulb possessedLightBulb = null;

    protected final int SLIME_EJECTION_RATE = Tick.TICK / 5;
    protected double lastSlimeEjection = 0;
    protected SlimeSound slimeSound = new SlimeSound();

    public int reviveTick;
    protected int strength;
    protected LightBulb lightBulb;


    protected double stunTick;

    protected double angle;

    protected final PlayerType TYPE;
    protected User user;
    protected final Paint BAR_BACKGROUND_COLOR;
    protected final Paint STRENGTH_BAR_COLOR;
    protected Bitmap PLAYER_BMP;
    protected Bitmap scaledPlayerBmp;
    protected final Bitmap STUN_BMP;
    protected  Bitmap scaledStunBmp;

    public Player(PlayerType type, Map map, byte team, byte playerId) {
        super(map.getStartX(team), map.getStartY(team));
        TYPE = type;
        if (type == PlayerType.FLUFFY){
            PLAYER_BMP = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.fluffy);
        }else if (type == PlayerType.SLIME){
            PLAYER_BMP = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.slime);
        }else if (type == PlayerType.GHOST){
            PLAYER_BMP = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.ghost);
        }else {
            PLAYER_BMP = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.ic_launcher);
        }
        scaledPlayerBmp = Bitmap.createScaledBitmap(PLAYER_BMP, Map.TILE_SIDE, Map.TILE_SIDE, false);
        STUN_BMP = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.stun_overlay);
        scaledStunBmp = Bitmap.createScaledBitmap(STUN_BMP, Map.TILE_SIDE,Map.TILE_SIDE, false);
        TEAM = team;
        BAR_HEIGHT = Map.TILE_SIDE / 5;
        BAR_BACKGROUND_COLOR = new Paint();
        BAR_BACKGROUND_COLOR.setColor(0x50000000);
        STRENGTH_BAR_COLOR = new Paint();
        user = GameThread.getUser();
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
        if (!invisible && !dead) {
            Matrix matrix = new Matrix();
            matrix.postRotate((float) (angle / 2 / Math.PI * 360) - 90);
            Bitmap rotated = Bitmap.createBitmap(scaledPlayerBmp, 0, 0, scaledPlayerBmp.getWidth(), scaledPlayerBmp.getHeight(), matrix, false);
            canvas.drawBitmap(rotated, (float)((xCoordinate - GameThread.getUser().getXCoordinate()) * Map.TILE_SIDE + GameSurface.getSurfaceWidth() / 2.0 - rotated.getWidth() / 2.0), (float)((yCoordinate - GameThread.getUser().getYCoordinate()) * Map.TILE_SIDE + GameSurface.getSurfaceHeight() / 2.0 - rotated.getHeight() / 2.0), null);        }
        if (stunned){
            canvas.drawBitmap(scaledStunBmp, (xCoordinate - GameThread.getUser().getXCoordinate() - playerRadius) * Map.TILE_SIDE + GameSurface.getSurfaceWidth() / 2, (yCoordinate - GameThread.getUser().getYCoordinate() - playerRadius) * Map.TILE_SIDE + GameSurface.getSurfaceHeight() / 2, null);
        }
        return canvas;
    }

    public void update(){
        if (stunned && stunTick <= GameThread.getSynchronizedTick()){
            stunned = false;
        }
        if (lightBulb != null){
            strength ++;
            if (strength >= MAX_STRENGTH){
                strength = MAX_STRENGTH;
            }
            if (strength <= 0) {
                bulbLost();
            }
        }
        if(slimy) {
            if (GameThread.getSynchronizedTick() - SLIME_EJECTION_RATE >= lastSlimeEjection) {
                lastSlimeEjection = GameThread.getSynchronizedTick();
                SlimeTrail slimeTrail = new SlimeTrail(getXCoordinate(), getYCoordinate());
                GameThread.addSlimeTrail(slimeTrail);
            }
        }
    }

    protected void death(){
        new DeathEvent(true).send();
        new DeathEvent(true).apply();
        reviveTick = (int) GameThread.getSynchronizedTick() + DEATH_TIME;
        setPlayerRadius(startRadius);
        new RadiusChangedEvent(startRadius).send();
    }

    public void stun(double stunTick){
        new StunSound().start(TIME_PER_TICK * STUN_TIME);
        stunned = true;
        this.stunTick = stunTick;
    }

    public SlimeSound getSlimeSound(){
        return slimeSound;
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
        lightBulb = GameThread.getLightBulbArray() [bulbID];
        lightBulb.setPossessor(this);
        flagPossessed = true;
    }

    public void bulbLost(){
        if(lightBulb != null) {
            lightBulb.fallOnFloor();
            lightBulb = null;
            flagPossessed = false;
        }
    }

    protected void putOnLightBulbStand(LightBulbStand lightBulbStand){
        lightBulb.putOnLightBulbStand(lightBulbStand);
        lightBulb = null;
    }

    public void setDead(boolean dead){
        this.dead = dead;
    }

    public boolean getDead(){
        return dead;
    }

    public void particleHit(){
        strength -=10;
    }

    public float getPlayerRadius(){
        return playerRadius;
    }

    public void setPlayerRadius(float radius){
        playerRadius = radius;
        try {
            scaledPlayerBmp = Bitmap.createScaledBitmap(PLAYER_BMP, (int) (playerRadius * 2 * Map.TILE_SIDE), (int) (playerRadius * 2 * Map.TILE_SIDE), false);
            scaledStunBmp = Bitmap.createScaledBitmap(STUN_BMP, (int) (playerRadius * 2 * Map.TILE_SIDE), (int) (playerRadius * 2 * Map.TILE_SIDE), false);
        } catch (IllegalArgumentException e){
            // this means the radius is 0, this happens when falling
        }
    }}