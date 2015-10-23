package ch.imlee.maturarbeit.game.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import ch.imlee.maturarbeit.events.gameActionEvents.DeathEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.LightBulbStandServerEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.ParticleShotEvent;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.events.gameActionEvents.LightBulbEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.LightBulbServerEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.RadiusChangedEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.SweetEatenEvent;
import ch.imlee.maturarbeit.game.map.LightBulbStand;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.events.gameActionEvents.PlayerMotionEvent;
import ch.imlee.maturarbeit.utils.Vector2D;
import ch.imlee.maturarbeit.views.GameSurface;
import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;

/**
 * Created by Sandro on 06.06.2015.
 */
public class User extends Player {

    private boolean falling = false;
    /**
     * is true when the particleButton is pressed
     */
    protected boolean shooting;
    /**
     * is true when the player has changed his facing direction(#angle) during the last tick
     */
    protected boolean angleChanged;
    /**
     * is true after the LightBulbServerEvent is sent until the lightBulbIsPickedUp
     */
    private boolean bulbRequestSent;

    private boolean standRequestSent = false;

    private byte requestedStandID;

    protected final int MAX_MANA = 1000;
    protected final int PARTICLE_COOL_DOWN = 1000 / TIME_PER_TICK;
    protected final int PICK_UP_RANGE = 2;
    protected final int PICK_UP_TICKS = 2 * Tick.TICK;
    protected int pickUpTickCount;
    private int weightLossCooldown = Tick.TICK * 3;

    protected final float START_X;
    protected final float START_Y;
    protected final float SLOW_AMOUNT = 1 / 2f;
    private final float FALLING_RADIUS_DECREASE = 0.5f / Tick.TICK;
    private final float MIN_RADIUS = START_RADIUS;
    private final float MAX_RADIUS = START_RADIUS *3;
    private final float RADIUS_CHANGE = 0.2f;
    protected float mana;
    protected float maxSpeed = 4f / Tick.TICK;
    //velocity determines how the far the player wants to travel in the next update and speed is the distance it travelled in the last update
    protected float velocity, speed;

    protected double particleCoolDownTick;
    private double lastWeightLoss = 0;

    protected final Paint SKILL_BAR_COLOR;
    protected final Paint PICK_UP_BAR_COLOR;
    protected LightBulb pickUpBulb = null;
    protected Vector2D newPosition;
    protected Map map;

    public User(PlayerType type, Map map, byte team, byte playerId) {
        super(type, map, team, playerId);
        START_X = xCoordinate;
        START_Y = yCoordinate;
        user = this;
        SKILL_BAR_COLOR = new Paint();
        PICK_UP_BAR_COLOR = new Paint();
        if (type == PlayerType.FLUFFY){
            SKILL_BAR_COLOR.setColor(0xff0000ff);
        }else if (type == PlayerType.GHOST){
            SKILL_BAR_COLOR.setColor(0xff000000);
        }else if (type == PlayerType.SLIME){
            SKILL_BAR_COLOR.setColor(0xff00ff00);
        }
        PICK_UP_BAR_COLOR.setColor(0xa0ffff00);
        this.map = map;
    }

    @Override
    public void update() {
        super.update();
        if(!dead) {
            if (!falling) {
                move();
                if (Map.TILE_MAP[(int) getXCoordinate()][(int) getYCoordinate()].FALL_THROUGH) {
                    //// TODO: 09.09.2015 if there is a 2*2 field (or bigger) of void tiles, you should be able to fall down if you have a radius >0.5 
                    if (xCoordinate % 1 >= 0.8f * playerRadius && xCoordinate + 0.8f * playerRadius <= (int) xCoordinate + 1 &&
                            yCoordinate % 1 >= 0.8f * playerRadius && yCoordinate + 0.8f * playerRadius <= (int) yCoordinate + 1) {
                        falling = true;
                    }
                }
                if (GameThread.getSynchronizedTick() - weightLossCooldown > lastWeightLoss) {
                    loseWeight();
                    lastWeightLoss = GameThread.getSynchronizedTick();
                }
                for (Sweet sweet : GameThread.sweets) {
                    if (Math.sqrt(Math.pow((double) (sweet.getXCoordinate() - this.getXCoordinate()), 2) + Math.pow((double) (sweet.getYCoordinate() - this.getYCoordinate()), 2)) < getPlayerRadius()) {
                        eatSweet(sweet);
                        lastWeightLoss = GameThread.getSynchronizedTick();
                    }
                }
                if (shooting && !stunned && particleCoolDownTick <= GameThread.getSynchronizedTick()) {
                    ParticleShotEvent particleShotEvent = new ParticleShotEvent(xCoordinate, yCoordinate, angle, GameThread.getSynchronizedTick(), GameThread.getCurrentFreeParticleID());
                    particleShotEvent.send();
                    particleShotEvent.apply();
                    particleCoolDownTick = GameThread.getSynchronizedTick() + PARTICLE_COOL_DOWN;
                }
                if (bulbRequestSent) {
                    if (lightBulb != null || !pickUpBulb.isPickable()) {
                        pickUpBulb = null;
                        pickUpTickCount = 0;
                        bulbRequestSent = false;
                    }
                } else if (pickUpBulb != null) {
                    pickUpTickCount++;
                    if (Math.pow(xCoordinate - pickUpBulb.getXCoordinate(), 2) + Math.pow(yCoordinate - pickUpBulb.getYCoordinate(), 2) > PICK_UP_RANGE * PICK_UP_RANGE) {
                        pickUpTickCount = 0;
                        pickUpBulb = null;
                    } else if (pickUpTickCount >= PICK_UP_TICKS) {
                        Log.i("User", "LightBulbServer sent");
                        if (StartActivity.deviceType == DeviceType.CLIENT) {
                            new LightBulbServerEvent(this, pickUpBulb.ID).send();
                        } else {
                            new LightBulbEvent(pickUpBulb.ID).send();
                            bulbReceived(pickUpBulb.ID);
                        }
                        bulbRequestSent = true;
                    }
                } else if (lightBulb == null) {
                    for (LightBulb lightBulb : GameThread.getLightBulbArray()) {
                        if (lightBulb.getLightBulbStandTeam() == TEAM || !lightBulb.isPickable()) {
                            continue;
                        }
                        if (Math.pow(xCoordinate - lightBulb.getXCoordinate(), 2) + Math.pow(yCoordinate - lightBulb.getYCoordinate(), 2) <= PICK_UP_RANGE * PICK_UP_RANGE) {
                            pickUpTickCount = 0;
                            pickUpBulb = lightBulb;
                            break;
                        }
                    }
                }
                if ((standRequestSent&&Map.getFriendlyLightBulbStands(TEAM)[requestedStandID].isFree())||lightBulb==null){
                    standRequestSent = false;
                }
                if (lightBulb != null) {
                    for (LightBulbStand lightBulbStand : Map.getFriendlyLightBulbStands(TEAM)) {
                        if (lightBulbStand.isFree() && Math.pow(xCoordinate - lightBulbStand.CENTER_X, 2) + Math.pow(xCoordinate - lightBulbStand.CENTER_X, 2) < PICK_UP_RANGE * PICK_UP_RANGE) {
                            if (StartActivity.deviceType == DeviceType.CLIENT) {
                                if (!standRequestSent) {
                                    new LightBulbStandServerEvent(lightBulbStand.ID).send();
                                    standRequestSent = true;
                                    requestedStandID = lightBulbStand.ID;
                                }
                            }else{
                                new LightBulbStandServerEvent(lightBulbStand.ID).apply();
                            }
                        }
                    }
                    if (strength <= 0) {
                        bulbLost();
                    }
                }
            } else {
                setPlayerRadius(playerRadius - FALLING_RADIUS_DECREASE);
                new RadiusChangedEvent(playerRadius).send();
                if (playerRadius <= 0.05) {
                    death();
                    falling = false;
                }
            }
        } else if(GameThread.getSynchronizedTick() >= reviveTick){
            dead = false;
            new DeathEvent(false).send();
        }
    }

    @Override
    public Canvas render(Canvas canvas){
        canvas = super.render(canvas);
        canvas.drawRect(0, GameSurface.getSurfaceHeight() - BAR_HEIGHT, GameSurface.getSurfaceWidth(), GameSurface.getSurfaceHeight(), BAR_BACKGROUND_COLOR);
        canvas.drawRect(0, GameSurface.getSurfaceHeight() - BAR_HEIGHT, GameSurface.getSurfaceWidth() * mana / MAX_MANA, GameSurface.getSurfaceHeight(), SKILL_BAR_COLOR);
        if (pickUpBulb != null){
            canvas.drawRect(0, 0, BAR_HEIGHT, GameSurface.getSurfaceHeight(), BAR_BACKGROUND_COLOR);
            canvas.drawRect(0, GameSurface.getSurfaceHeight() - GameSurface.getSurfaceHeight() * pickUpTickCount / PICK_UP_TICKS, BAR_HEIGHT, GameSurface.getSurfaceHeight(), PICK_UP_BAR_COLOR);
        }
        return  canvas;
    }

    private void move() {
        float tempVelocity = processedVelocity();
        if (stunned || tempVelocity == 0) {
            speed = 0;
            return;
        }
        newPosition = new Vector2D((float) (xCoordinate + Math.cos(angle) * tempVelocity * maxSpeed), (float) (yCoordinate + Math.sin(angle) * tempVelocity * maxSpeed));
        physicEngine();
        speed= (float) Math.sqrt(Math.pow(xCoordinate - newPosition.x,2)+Math.pow(yCoordinate - newPosition.y,2));
        if (xCoordinate != newPosition.x || yCoordinate != newPosition.y) {
            xCoordinate = newPosition.x;
            yCoordinate = newPosition.y;
            new PlayerMotionEvent(this).send();
        }
    }

    protected float processedVelocity(){
        for (SlimeTrail slimeTrail:GameThread.getSlimeTrailList()){
            if (Math.pow(xCoordinate - slimeTrail.getXCoordinate(), 2) + Math.pow(yCoordinate - slimeTrail.getYCoordinate(), 2) <= Math.pow(playerRadius + SlimeTrail.TRAIL_RADIUS, 2)){
                if (TYPE==PlayerType.SLIME) {
                    return velocity / SLOW_AMOUNT;
                }else{
                    return velocity * SLOW_AMOUNT;
                }
            }
        }
        return velocity;
    }

    private void physicEngine(){
        if (Map.getSolid((int) (newPosition.x + playerRadius), newPosition.yIntPos())) {
            newPosition.x = (int) (newPosition.x) - playerRadius + 1;
        }
        if (Map.getSolid((int) (newPosition.x - playerRadius), newPosition.yIntPos())) {
            newPosition.x = newPosition.xIntPos() + playerRadius;
        }
        if (Map.getSolid(newPosition.xIntPos(), (int) (newPosition.y + playerRadius))) {
            newPosition.y = newPosition.yIntPos() - playerRadius + 1;
        }
        if (Map.getSolid(newPosition.xIntPos(), (int) (newPosition.y - playerRadius))) {
            newPosition.y = newPosition.yIntPos() + playerRadius;
        }
        Vector2D tempVec;
        float l;
        //bottom right quadrant
        if (Map.getSolid(newPosition.xIntPos() + 1, newPosition.yIntPos() + 1)) {
            tempVec = new Vector2D(newPosition.xIntPos() + 1, newPosition.yIntPos() + 1, newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        if (Map.getSolid(newPosition.xIntPos() + 2, newPosition.yIntPos() + 1)) {
            tempVec = new Vector2D(newPosition.xIntPos() + 2, newPosition.yIntPos() + 1, newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        if (Map.getSolid(newPosition.xIntPos() + 1, newPosition.yIntPos() + 2)) {
            tempVec = new Vector2D(newPosition.xIntPos() + 1, newPosition.yIntPos() + 2, newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        if (Map.getSolid(newPosition.xIntPos() + 2, newPosition.yIntPos() + 2)) {
            tempVec = new Vector2D(newPosition.xIntPos() + 2, newPosition.yIntPos() + 2, newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        //top right quadrant
        if (Map.getSolid(newPosition.xIntPos() + 1, newPosition.yIntPos() - 1)) {
            tempVec = new Vector2D(newPosition.xIntPos() + 1, newPosition.yIntPos(), newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        if (Map.getSolid(newPosition.xIntPos() + 2, newPosition.yIntPos() - 1)) {
            tempVec = new Vector2D(newPosition.xIntPos() + 2, newPosition.yIntPos(), newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        if (Map.getSolid(newPosition.xIntPos() + 1, newPosition.yIntPos() - 2)) {
            tempVec = new Vector2D(newPosition.xIntPos() + 1, newPosition.yIntPos() - 1, newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        if (Map.getSolid(newPosition.xIntPos() + 2, newPosition.yIntPos() - 2)) {
            tempVec = new Vector2D(newPosition.xIntPos() + 2, newPosition.yIntPos() - 1, newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        //top left quadrant
        if (Map.getSolid(newPosition.xIntPos() - 1, newPosition.yIntPos() - 1)) {
            tempVec = new Vector2D(newPosition.xIntPos(), newPosition.yIntPos(), newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        if (Map.getSolid(newPosition.xIntPos() - 2, newPosition.yIntPos() - 1)) {
            tempVec = new Vector2D(newPosition.xIntPos() - 1, newPosition.yIntPos(), newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        if (Map.getSolid(newPosition.xIntPos() - 1, newPosition.yIntPos() - 2)) {
            tempVec = new Vector2D(newPosition.xIntPos(), newPosition.yIntPos() - 1, newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        if (Map.getSolid(newPosition.xIntPos() - 2, newPosition.yIntPos() - 2)) {
            tempVec = new Vector2D(newPosition.xIntPos() - 1, newPosition.yIntPos() - 1, newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        //bottom left quadrant
        if (Map.getSolid(newPosition.xIntPos() - 1, newPosition.yIntPos() + 1)) {
            tempVec = new Vector2D(newPosition.xIntPos(), newPosition.yIntPos() + 1, newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        if (Map.getSolid(newPosition.xIntPos() - 2, newPosition.yIntPos() + 1)) {
            tempVec = new Vector2D(newPosition.xIntPos() - 1, newPosition.yIntPos() + 1, newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        if (Map.getSolid(newPosition.xIntPos() - 1, newPosition.yIntPos() + 2)) {
            tempVec = new Vector2D(newPosition.xIntPos(), newPosition.yIntPos() + 2, newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
        if (Map.getSolid(newPosition.xIntPos() - 2, newPosition.yIntPos() + 2)) {
            tempVec = new Vector2D(newPosition.xIntPos() - 1, newPosition.yIntPos() + 2, newPosition.x, newPosition.y);
            l = (float) tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.x += tempVec.x;
                newPosition.y += tempVec.y;
            }
        }
    }

    private void loseWeight(){
        float oldRadius = playerRadius;
        setPlayerRadius(getPlayerRadius() - RADIUS_CHANGE);
        if(getPlayerRadius() < MIN_RADIUS){
            setPlayerRadius(MIN_RADIUS);
        }
        if(playerRadius != oldRadius) new RadiusChangedEvent(getPlayerRadius()).send();
    }

    public void eatSweet(Sweet sweet){
        setPlayerRadius(getPlayerRadius() + RADIUS_CHANGE);
        if(getPlayerRadius() > MAX_RADIUS){
            setPlayerRadius(MAX_RADIUS);
        }
        // handle collisions
        RadiusChangedEvent radiusChangedEvent = new RadiusChangedEvent(getPlayerRadius());
        radiusChangedEvent.send();
        SweetEatenEvent sweetEatenEvent = new SweetEatenEvent(sweet);
        sweetEatenEvent.send();
        sweetEatenEvent.apply();
    }

    @Override
    protected void death() {
        super.death();
        bulbLost();
    }

    public void skillActivation(){

    }

    public void setShooting(boolean isShooting){
        shooting = isShooting;
    }

    public void setVelocity(float velocity){
        this.velocity = velocity;
    }

    @Override
    public void bulbLost() {
        if (lightBulb != null) {
            new LightBulbEvent(lightBulb.ID, ID).send();
            super.bulbLost();
        }
    }


    public double getAngle(){
        return angle;
    }

    @Override
    public void setAngle(double angle) {
        if (angle != this.angle) {
            angleChanged = true;
        }
        super.setAngle(angle);
    }
}
