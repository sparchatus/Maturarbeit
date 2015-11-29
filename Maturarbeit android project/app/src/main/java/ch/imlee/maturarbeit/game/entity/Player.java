package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.activities.GameClient;
import ch.imlee.maturarbeit.events.gameActionEvents.DeathEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.RadiusChangedEvent;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Sound.StunSound;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.utils.LogView;
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
    protected int strength;

    public static final float START_RADIUS = 0.4f;
    protected float playerRadius = START_RADIUS;

    protected double lastSlimeEjection = 0;
    protected double stunTick;
    protected double angle;

    public final String NAME;
    private static Paint namePaint = new Paint();

    protected final Paint BAR_BACKGROUND_COLOR;
    protected final Paint STRENGTH_BAR_COLOR;
    protected final Bitmap STUN_BMP;
    protected final PlayerType TYPE;
    protected LightBulb lightBulb;
    protected static User user;
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
        scaledPlayerBmp = Bitmap.createScaledBitmap(PLAYER_BMP, Map.TILE_SIDE, Map.TILE_SIDE, false);
        STUN_BMP = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.stun_overlay);
        scaledStunBmp = Bitmap.createScaledBitmap(STUN_BMP, Map.TILE_SIDE,Map.TILE_SIDE, false);
        TEAM = team;
        BAR_HEIGHT = Map.TILE_SIDE / 4;
        BAR_BACKGROUND_COLOR = new Paint();
        BAR_BACKGROUND_COLOR.setColor(0x50000000);
        STRENGTH_BAR_COLOR = new Paint();
        if (user == null || TEAM == user.TEAM){
            STRENGTH_BAR_COLOR.setColor(0xff00ff00);
        }else {
            STRENGTH_BAR_COLOR.setColor(0xffff0000);
        }
        this.ID = playerId;
        NAME = name;
        namePaint.setTextAlign(Paint.Align.CENTER);
    }


    public void update(){
        // the Player removes the stun at the right time
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
    public Canvas render(Canvas canvas){
        // if the Player isn't invisible nor dead he is rendered
        if (!invisible && !dead) {
            // rotating the Player Bitmap before rendering it.
            Matrix matrix = new Matrix();
            matrix.postRotate((float) (angle / 2 / Math.PI * 360) - 90);
            Bitmap rotated = Bitmap.createBitmap(scaledPlayerBmp, 0, 0, scaledPlayerBmp.getWidth(), scaledPlayerBmp.getHeight(), matrix, false);

            canvas.drawBitmap(rotated, (xCoordinate - GameThread.getUser().getXCoordinate()) * Map.TILE_SIDE + GameSurface.getSurfaceWidth() / 2f - rotated.getWidth() / 2f, (yCoordinate - GameThread.getUser().getYCoordinate()) * Map.TILE_SIDE + GameSurface.getSurfaceHeight() / 2f - rotated.getHeight() / 2f, null);        }
        // if the Player is stunned he gets an overlay
        if (stunned){
            canvas.drawBitmap(scaledStunBmp, (xCoordinate - GameThread.getUser().getXCoordinate() - playerRadius) * Map.TILE_SIDE + GameSurface.getSurfaceWidth() / 2f, (yCoordinate - GameThread.getUser().getYCoordinate() - playerRadius) * Map.TILE_SIDE + GameSurface.getSurfaceHeight() / 2f, null);
        }
        // if the Player is in possession of a LightBulb, he renders his strength bar below himself
        if (lightBulb != null) {
            canvas.drawRect((xCoordinate - GameThread.getUser().getXCoordinate() - playerRadius) * Map.TILE_SIDE + GameSurface.getSurfaceWidth() / 2f,(yCoordinate - GameThread.getUser().getYCoordinate() + playerRadius) * Map.TILE_SIDE + GameSurface.getSurfaceHeight() / 2f,(xCoordinate - GameThread.getUser().getXCoordinate() + playerRadius) * Map.TILE_SIDE + GameSurface.getSurfaceWidth() / 2f,(yCoordinate - GameThread.getUser().getYCoordinate() + playerRadius) * Map.TILE_SIDE + GameSurface.getSurfaceHeight() / 2f + BAR_HEIGHT, BAR_BACKGROUND_COLOR);
            canvas.drawRect((xCoordinate - GameThread.getUser().getXCoordinate() - playerRadius) * Map.TILE_SIDE + GameSurface.getSurfaceWidth() / 2f,(yCoordinate - GameThread.getUser().getYCoordinate() + playerRadius) * Map.TILE_SIDE + GameSurface.getSurfaceHeight() / 2f,(xCoordinate - GameThread.getUser().getXCoordinate() - playerRadius) * Map.TILE_SIDE + 2 * playerRadius * Map.TILE_SIDE * strength / MAX_STRENGTH + GameSurface.getSurfaceWidth() / 2f,(yCoordinate - GameThread.getUser().getYCoordinate() + playerRadius) * Map.TILE_SIDE + GameSurface.getSurfaceHeight() / 2f + BAR_HEIGHT, STRENGTH_BAR_COLOR);
        }
        // render the name
        // team members should have a green nametag, enemies a red one
        if(this.getTeam()==user.getTeam()){
            namePaint.setColor(Color.GREEN);
        } else{
            namePaint.setColor(Color.RED);
        }
        namePaint.setTextSize(this.getPlayerRadius()*Map.TILE_SIDE);
        // don't draw the name for your own Player
        if(this.getID() != user.getID()) canvas.drawText(NAME, (xCoordinate - GameThread.getUser().getXCoordinate())*Map.TILE_SIDE + GameSurface.getSurfaceWidth()/2, (yCoordinate - GameThread.getUser().getYCoordinate())*Map.TILE_SIDE + GameSurface.getSurfaceHeight()/2 + namePaint.getTextSize()/2, namePaint);



        return canvas;
    }

    // getting stunned activates stunned, plays a sound and activates the stun timer
    public void stun(double stunTick){
        new StunSound().start(TIME_PER_TICK * STUN_TIME);
        stunned = true;
        this.stunTick = stunTick;
    }

    public static void setUser(User user1){
        user = user1;
    }

    public void particleHit(){
        strength -= 10;
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

    public boolean getDead(){
        return dead;
    }

    public float getPlayerRadius(){
        return playerRadius;
    }

    public void setPlayerRadius(float radius){
        if(radius < 0.01f) radius = 0.01f;
        playerRadius = radius;
        scaledPlayerBmp = Bitmap.createScaledBitmap(PLAYER_BMP, (int) (playerRadius * 2 * Map.TILE_SIDE), (int) (playerRadius * 2 * Map.TILE_SIDE), false);
        scaledStunBmp = Bitmap.createScaledBitmap(STUN_BMP, (int) (playerRadius * 2 * Map.TILE_SIDE), (int) (playerRadius * 2 * Map.TILE_SIDE), false);
    }
}