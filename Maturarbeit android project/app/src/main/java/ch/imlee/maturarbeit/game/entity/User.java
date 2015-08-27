package ch.imlee.maturarbeit.game.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameServerThread;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.events.gameActionEvents.LightBulbEvent;
import ch.imlee.maturarbeit.game.events.gameActionEvents.LightBulbServerEvent;
import ch.imlee.maturarbeit.game.events.gameActionEvents.ParticleServerEvent;
import ch.imlee.maturarbeit.game.events.gameActionEvents.ParticleShotEvent;
import ch.imlee.maturarbeit.game.events.gameActionEvents.RadiusChangedEvent;
import ch.imlee.maturarbeit.game.events.gameActionEvents.SweetEatenEvent;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.events.gameActionEvents.PlayerMotionEvent;
import ch.imlee.maturarbeit.game.views.GameSurface;
import ch.imlee.maturarbeit.main.DeviceType;
import ch.imlee.maturarbeit.main.StartActivity;

/**
 * Created by Sandro on 06.06.2015.
 */
public class User extends Player {

    protected final Paint SKILL_BAR_COLOR;
    protected final Paint PICK_UP_BAR_COLOR;
    protected final int MAX_MANA = 1000;
    protected final float MAX_SPEED = 4f / Tick.TICK;
    protected final int PARTICLE_COOL_DOWN = 500 / TIME_PER_TICK;
    protected final int PICK_UP_RANGE = 2;
    protected final int PICK_UP_TICKS = 2 * Tick.TICK;
    protected final float SLOW_AMOUNT = 1 / 2f;
    protected LightBulb pickUpBulb = null;

    private final float MIN_RADIUS = getPlayerRadius();
    private final float MAX_RADIUS = getPlayerRadius()*3;
    private final float RADIUS_CHANGE = 0.2f;
    private double lastWeightLoss = 0;
    private int weightLossCooldown = Tick.TICK * 3;

    protected boolean shooting;

    protected int pickUpTickCount;

    protected double particleCoolDownTick;

    protected float mana;

    //velocity determines how the far the player wants to travel in the next update and speed is the distance it travelled in the last update, angle os the angle from the last update
    protected float velocity, speed, oldAngle;
    protected Map map;

    public User(PlayerType type, Map map, byte team, byte playerId) {
        super(type, map, team, playerId);
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
        move();
        if(GameThread.getSynchronizedTick() - weightLossCooldown > lastWeightLoss){
            //loseWeight();
            lastWeightLoss = GameThread.getSynchronizedTick();
        }
        for(Sweet sweet:GameThread.sweets){
            if(Math.sqrt(Math.pow((double)(sweet.getXCoordinate()-this.getXCoordinate()), 2) + Math.pow((double)(sweet.getYCoordinate()-this.getYCoordinate()), 2)) < getPlayerRadius()){
                eatSweet(sweet);
            }
        }
        if (shooting && particleCoolDownTick <= GameThread.getSynchronizedTick() && !stunned){
            if (StartActivity.deviceType == DeviceType.CLIENT){
                new ParticleServerEvent(this, GameThread.getSynchronizedTick()).send();
            }else{
                ParticleShotEvent particleShotEvent = new ParticleShotEvent(this, (int)GameThread.getSynchronizedTick(), GameServerThread.getCurrentParticleID());
                particleShotEvent.send();
                particleShotEvent.apply();
            }
            particleCoolDownTick = GameThread.getSynchronizedTick() + PARTICLE_COOL_DOWN;
        }
        if (pickUpBulb != null){
            pickUpTickCount++;
            if (Math.sqrt(Math.pow(xCoordinate - pickUpBulb.getXCoordinate(), 2) + Math.pow(yCoordinate - pickUpBulb.getYCoordinate(), 2)) > PICK_UP_RANGE) {
                pickUpTickCount = 0;
                pickUpBulb = null;
            } else if (pickUpTickCount >= PICK_UP_TICKS){
                new LightBulbServerEvent(this, pickUpBulb.getLIGHT_BULB_ID()).send();
                pickUpBulb = null;
                pickUpTickCount = 0;
            }
        } else {
            for (LightBulb lightBulb:GameThread.getLightBulbArray()) {
                if (lightBulb.getPossessor() == null && Math.sqrt(Math.pow(xCoordinate - lightBulb.getXCoordinate(), 2) + Math.pow(yCoordinate - lightBulb.getYCoordinate(), 2)) <= PICK_UP_RANGE){
                    pickUpBulb = lightBulb;
                    break;
                }
            }
        }
        if (flagPossessed){
            strength ++;
            if (strength >= MAX_STRENGTH){
                strength = MAX_STRENGTH;
            }
            if (strength <= 0) {
                bulbLost();
            }
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
        float newXCoordinate = (float) (xCoordinate + Math.cos(angle) * velocity * MAX_SPEED);
        float newYCoordinate = (float) (yCoordinate + Math.sin(angle) * velocity * MAX_SPEED);
        if(map.getSolid((int)(newXCoordinate - 0.5), (int)newYCoordinate) || map.getSolid((int)(newXCoordinate + 0.5), (int)newYCoordinate)){
            newXCoordinate = (int)newXCoordinate + 0.5f;
        }
        if(map.getSolid((int)newXCoordinate, (int)(newYCoordinate - 0.5)) || map.getSolid((int)newXCoordinate, (int)(newYCoordinate + 0.5))){
            newYCoordinate = (int)newYCoordinate + 0.5f;
        }
        speed = (float) Math.sqrt(Math.pow((newXCoordinate - xCoordinate) / MAX_SPEED, 2) + Math.pow((newYCoordinate - yCoordinate) / MAX_SPEED, 2));
        if(xCoordinate != newXCoordinate || yCoordinate != newYCoordinate ||oldAngle != angle) {
            xCoordinate = newXCoordinate;
            yCoordinate = newYCoordinate;
            new PlayerMotionEvent(this).send();
        }
    }

    private void loseWeight(){
        setPlayerRadius(getPlayerRadius()-RADIUS_CHANGE);
        if(getPlayerRadius() < MIN_RADIUS){
            setPlayerRadius(MIN_RADIUS);
        }
        // send SetRadiusEvent
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
        super.bulbLost();
        new LightBulbEvent(possessedLightBulb.getLIGHT_BULB_ID(), ID).send();
    }
}
