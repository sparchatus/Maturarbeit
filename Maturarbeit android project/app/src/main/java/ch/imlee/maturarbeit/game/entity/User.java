package ch.imlee.maturarbeit.game.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import ch.imlee.maturarbeit.events.gameActionEvents.DeathEvent;
import ch.imlee.maturarbeit.game.GameServerThread;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.events.gameActionEvents.LightBulbEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.LightBulbServerEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.ParticleServerEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.ParticleShotEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.RadiusChangedEvent;
import ch.imlee.maturarbeit.events.gameActionEvents.SweetEatenEvent;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.events.gameActionEvents.PlayerMotionEvent;
import ch.imlee.maturarbeit.views.GameSurface;
import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;

/**
 * Created by Sandro on 06.06.2015.
 */
public class User extends Player {

    protected final float START_X;
    protected final float START_Y;

    protected final Paint SKILL_BAR_COLOR;
    protected final Paint PICK_UP_BAR_COLOR;
    protected final int MAX_MANA = 1000;
    protected final int PARTICLE_COOL_DOWN = 1000 / TIME_PER_TICK;
    protected final int PICK_UP_RANGE = 2;
    protected final int PICK_UP_TICKS = 2 * Tick.TICK;
    protected final float SLOW_AMOUNT = 1 / 2f;
    protected LightBulb pickUpBulb = null;

    private final float MIN_RADIUS = startRadius;
    private final float MAX_RADIUS = startRadius *3;
    private final float RADIUS_CHANGE = 0.2f;
    private double lastWeightLoss = 0;
    private int weightLossCooldown = Tick.TICK * 3;

    private boolean falling = false;
    private final float FALLING_RADIUS_DECREASE = 0.5f / Tick.TICK;

    protected boolean shooting;

    protected int pickUpTickCount;
    private float newXCoordinate, newYCoordinate;
    protected float maxSpeed = 4f / Tick.TICK;

    protected double particleCoolDownTick;

    protected boolean angleChanged;
    protected float mana;

    //velocity determines how the far the player wants to travel in the next update and speed is the distance it travelled in the last update
    protected float velocity, speed;
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
                if (shooting && particleCoolDownTick <= GameThread.getSynchronizedTick() && !stunned) {
                    if (StartActivity.deviceType == DeviceType.CLIENT) {
                        new ParticleServerEvent(this, GameThread.getSynchronizedTick()).send();
                    } else {
                        ParticleShotEvent particleShotEvent = new ParticleShotEvent(this, (int) GameThread.getSynchronizedTick(), GameServerThread.getCurrentParticleID());
                        particleShotEvent.send();
                        particleShotEvent.apply();
                    }
                    particleCoolDownTick = GameThread.getSynchronizedTick() + PARTICLE_COOL_DOWN;
                }
                if (pickUpBulb != null) {
                    pickUpTickCount++;
                    if (Math.sqrt(Math.pow(xCoordinate - pickUpBulb.getXCoordinate(), 2) + Math.pow(yCoordinate - pickUpBulb.getYCoordinate(), 2)) > PICK_UP_RANGE) {
                        pickUpTickCount = 0;
                        pickUpBulb = null;
                    } else if (pickUpTickCount >= PICK_UP_TICKS) {
                        new LightBulbServerEvent(this, pickUpBulb.ID).send();
                        pickUpBulb = null;
                        pickUpTickCount = 0;
                    }
                } else {
                    for (LightBulb lightBulb : GameThread.getLightBulbArray()) {
                        if (lightBulb.isPickable() && lightBulb.getLightBulbStandTeam() != TEAM && Math.sqrt(Math.pow(xCoordinate - lightBulb.getXCoordinate(), 2) + Math.pow(yCoordinate - lightBulb.getYCoordinate(), 2)) <= PICK_UP_RANGE) {
                            pickUpBulb = lightBulb;
                            break;
                        }
                    }
                }
                if (flagPossessed) {
                    strength++;
                    if (strength >= MAX_STRENGTH) {
                        strength = MAX_STRENGTH;
                    }
                    if (strength <= 0) {
                        bulbLost();
                    }
                }
            } else {
                setPlayerRadius(playerRadius - FALLING_RADIUS_DECREASE);
                new RadiusChangedEvent(playerRadius).send();
                if (playerRadius <= 0) {
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
        //TODO better hit boxes with walls
        //TODO players can fall out of the world
        for (SlimeTrail slimeTrail:GameThread.getSlimeTrailList()){
            if (Math.sqrt(Math.pow(xCoordinate - slimeTrail.getXCoordinate(), 2) + Math.pow(yCoordinate - slimeTrail.getYCoordinate(), 2)) <= playerRadius + SlimeTrail.TRAIL_RADIUS){
                velocity*= SLOW_AMOUNT;
                break;
            }
        }
        if (stunned || velocity == 0) {
            speed = 0;
            return;
        }
        newXCoordinate = (float) (xCoordinate + Math.cos(angle) * velocity * maxSpeed);
        newYCoordinate = (float) (yCoordinate + Math.sin(angle) * velocity * maxSpeed);
        physicEngine();
        speed = (float) Math.sqrt(Math.pow(newXCoordinate - xCoordinate, 2) + Math.pow(newYCoordinate - yCoordinate, 2));
        if (xCoordinate != newXCoordinate || yCoordinate != newYCoordinate) {
            xCoordinate = newXCoordinate;
            yCoordinate = newYCoordinate;
            new PlayerMotionEvent(this).send();
        }
    }

    private void physicEngine(){
        if (Map.getSolid((int)(newXCoordinate + playerRadius), (int)newYCoordinate)){
            newXCoordinate = (int)(newXCoordinate) - playerRadius + 1;
        }
        if (Map.getSolid((int)(newXCoordinate - playerRadius), (int)newYCoordinate)){
            newXCoordinate = (int)(newXCoordinate) + playerRadius;
        }
        if (Map.getSolid((int)newXCoordinate, (int)(newYCoordinate + playerRadius))){
            newYCoordinate = (int)(newYCoordinate) - playerRadius + 1;
        }
        if (Map.getSolid((int)newXCoordinate, (int)(newYCoordinate - playerRadius))){
            newYCoordinate = (int)(newYCoordinate) + playerRadius;
        }
    }

    private void loseWeight(){
        float oldRadius = playerRadius;
        setPlayerRadius(getPlayerRadius()-RADIUS_CHANGE);
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
        new LightBulbEvent(possessedLightBulb.ID, ID).send();
        super.bulbLost();
    }

    public void angleHasChanged(){
        angleChanged = true;
    }

    public double getAngle(){
        return angle;
    }
}
