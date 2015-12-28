package ch.imlee.maturarbeit.game.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

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
import ch.imlee.maturarbeit.game.special_screens.DeathScreen;
import ch.imlee.maturarbeit.utils.Vector2D;
import ch.imlee.maturarbeit.views.GameSurface;
import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;

public class User extends Player {

    protected boolean falling = false;
    //is true when the particleButton is pressed
    protected boolean shooting;
    protected boolean angleChanged;
    // is true after the LightBulbServerEvent is sent until he or someone else receives the requested LightBulb
    private boolean bulbRequestSent;
    // is true after the LightBulbStandServerEvent was sent until the player can try again
    private boolean standRequestSent = false;

    // used to test if the put on stand was successful
    private byte requestedStandID;

    // the maximum amount of mana a player can store
    protected final int MAX_MANA = 1000;
    protected final int PARTICLE_COOL_DOWN = 400 / TIME_PER_TICK;
    // the range in which the LightBulb can be picked up
    protected final float PICK_UP_RANGE = 1.5f;
    // the time it takes to pick up the LightBulb
    protected final int PICK_UP_TICKS = 3 * Tick.TICK;
    private final int DEATH_TIME = 5000 / Tick.TIME_PER_TICK;
    // the time already passed since starting to pick up the LightBulb
    private final int weightLossCoolDown = Tick.TICK * 5;
    private int reviveTick;
    protected int pickUpTickCount;

    // location of the User at the start of the game
    protected final float START_X;
    protected final float START_Y;
    // the amount of slow SlimeTrails apply
    protected final float SLOW_AMOUNT = 2 / 3f;
    private final float FALLING_RADIUS_DECREASE = 0.5f / Tick.TICK;
    private final float MAX_RADIUS = 1;
    private final float RADIUS_CHANGE = 0.05f;
    private final float FALL_DETECTION_TOLERANCE = 0.7f;
    protected float mana;
    // MAX_SPEED / SLOW_AMOUNT < MIN_RADIUS has to be true!
    protected final float MAX_SPEED = 4.5f / Tick.TICK;
    //velocity determines how the far the player wants to travel in the next update and speed (only used by Fluffy) is the distance it travelled in the last update
    protected float velocity, speed;

    // next time a shot is allowed
    protected double particleCoolDownTick;
    private double lastWeightLoss = 0;

    protected final Paint SKILL_BAR_COLOR;
    protected final Paint PICK_UP_BAR_COLOR;
    protected LightBulb pickUpBulb = null;
    protected double minLightBulbPickupTick = 0;
    // for the resolution of the Hit box when moving
    protected Vector2D newPosition;

    public User(PlayerType type, Map map, byte team, byte playerId, String name) {
        super(type, map, team, playerId, name);
        START_X = xCoordinate;
        START_Y = yCoordinate;
        user = this;
        SKILL_BAR_COLOR = new Paint();
        PICK_UP_BAR_COLOR = new Paint();
        if (type == PlayerType.FLUFFY){
            // blue
            SKILL_BAR_COLOR.setColor(0xff0000ff);
        }else if (type == PlayerType.GHOST){
            //  white
            SKILL_BAR_COLOR.setColor(0xffffffff);
        }else if (type == PlayerType.SLIME){
            // green
            SKILL_BAR_COLOR.setColor(0xff00ff00);
        }
        // some kind transparent grey
        PICK_UP_BAR_COLOR.setColor(0xa0ffff00);
    }

    @Override
    public void update() {
        // a dead User isn't able to do anything
        if (dead) {
            // if the death time is over, tell the other devices that tis player revived
            if (GameThread.getSynchronizedTick() >= reviveTick) {
                dead = false;
                xCoordinate = START_X;
                yCoordinate = START_Y;
                new PlayerMotionEvent(this).send();
                new DeathEvent(false).send();
            }
            return;
        }
        super.update();
        // also a falling Player isn't able to do anything
        if (!falling) {
            // if the User is on a sweet he eats it
            for (Sweet sweet : GameThread.sweets) {
                if (Math.sqrt(Math.pow((double) (sweet.getXCoordinate() - this.getXCoordinate()), 2) + Math.pow((double) (sweet.getYCoordinate() - this.getYCoordinate()), 2)) < getPlayerRadius()) {
                    eatSweet(sweet);
                    lastWeightLoss = GameThread.getSynchronizedTick();
                }
            }
            // this includes: moving and resolving collisions with walls
            move();
            // checks if all the floor Tiles below the User are FALL_TROUGH
            checkFloor();
            // every now an then the Player looses weight/gets smaller
            if (GameThread.getSynchronizedTick() - weightLossCoolDown > lastWeightLoss) {
                loseWeight();
                lastWeightLoss = GameThread.getSynchronizedTick();
            }
            if (shooting && !stunned && particleCoolDownTick <= GameThread.getSynchronizedTick()) {
                shoot(angle);
            }
            lightBulbInteraction();
        }
        //if the Player is falling, his radius rapidly decreases which has to be told to the other devices
        else {
            setPlayerRadius(playerRadius - FALLING_RADIUS_DECREASE);
            new RadiusChangedEvent(playerRadius).send();
            // after some time falling the Player dies
            if (playerRadius <= 0.05) {
                death(" FELL");
                falling = false;
            }
        }
    }

    @Override
    public void render(Canvas canvas){
        // draw the mana bar
        super.render(canvas);
        canvas.drawRect(0, GameSurface.getSurfaceHeight() - BAR_HEIGHT, GameSurface.getSurfaceWidth(), GameSurface.getSurfaceHeight(), BAR_BACKGROUND_COLOR);
        canvas.drawRect(0, GameSurface.getSurfaceHeight() - BAR_HEIGHT, GameSurface.getSurfaceWidth() * mana / MAX_MANA, GameSurface.getSurfaceHeight(), SKILL_BAR_COLOR);
        // if the User is picking up a LightBulb, this draws the progress bar
        if (pickUpBulb != null){
            canvas.drawRect(0, 0, BAR_HEIGHT, GameSurface.getSurfaceHeight(), BAR_BACKGROUND_COLOR);
            canvas.drawRect(0, GameSurface.getSurfaceHeight() - GameSurface.getSurfaceHeight() * pickUpTickCount / PICK_UP_TICKS, BAR_HEIGHT, GameSurface.getSurfaceHeight(), PICK_UP_BAR_COLOR);
        }
    }

    private void move() {
        float tempVelocity = processedVelocity();
        // if the user is stunned or at rest no further calculations are necessary
        if (stunned || tempVelocity == 0) {
            speed = 0;
            // informing other devices when the Player has changed it's facing direction even though he didn't move
            if(angleChanged){
                new PlayerMotionEvent(this).send();
                angleChanged = false;
            }
            return;
        }

        // the desired position of the User
        newPosition = new Vector2D((float) (xCoordinate + Math.cos(angle) * tempVelocity * MAX_SPEED), (float) (yCoordinate + Math.sin(angle) * tempVelocity * MAX_SPEED));
        // detecting and resolving collisions with walls
        hitboxResolution();
        // calculating the distance the User travelled
        speed = (float) Math.sqrt(Math.pow(xCoordinate - newPosition.x,2)+Math.pow(yCoordinate - newPosition.y,2));

        // if the User has moved the coordinates get updated and the other devices get informed
        if (xCoordinate != newPosition.x || yCoordinate != newPosition.y) {

            xCoordinate = (float)newPosition.x;
            yCoordinate = (float)newPosition.y;
            new PlayerMotionEvent(this).send();
        }
    }

    // if the User is standing on a SlimeTrail, he gets slowed
    protected float processedVelocity(){
        for (SlimeTrail slimeTrail:GameThread.getSlimeTrailList()){
            if (Math.pow(xCoordinate - slimeTrail.getXCoordinate(), 2) + Math.pow(yCoordinate - slimeTrail.getYCoordinate(), 2) <= Math.pow(playerRadius - slimeTrail.TRAIL_RADIUS / Map.TILE_SIDE, 2)){
                if (TYPE==PlayerType.SLIME) {
                    return velocity / SLOW_AMOUNT;
                }else{
                    return velocity * SLOW_AMOUNT;
                }
            }
        }
        return velocity;
    }

    // detecting and resolving collisions with walls
    // the different checks aren't in a for loop to make it more readable
    private void hitboxResolution(){
        //these variable tels if any of the sides was hit which would lead to the diagonal tiles to be unable to be hit
        boolean sideHit = false;

        // checks the wall to the right of the User
        if (Map.getSolid((int) (newPosition.x + playerRadius), newPosition.yIntPos())) {
            newPosition.addX(1 - newPosition.xMod1() - playerRadius);
            sideHit = true;
        }
        // checks the wall to the left of the User
        else if (Map.getSolid((int) (newPosition.x - playerRadius), newPosition.yIntPos())) {
            newPosition.addX(-newPosition.xMod1() + playerRadius);
            sideHit = true;
        }
        // checks the wall at the bottom of the User
        if (Map.getSolid(newPosition.xIntPos(), (int) (newPosition.y + playerRadius))) {
            newPosition.addY(1 - newPosition.yMod1() - playerRadius);
            sideHit = true;
        }
        // checks the wall at the top of the User
        else if (Map.getSolid(newPosition.xIntPos(), (int) (newPosition.y - playerRadius))) {
            newPosition.addY(-newPosition.yMod1() + playerRadius);
            sideHit = true;
        }
        if (sideHit){
            return;
        }
        // variable to determine the amount by which the User is inside a block
        double l;
        // temp Vec is the connection of a wall edge to the User center it gets scaled to length l
        Vector2D tempVec;
        // checks the wall to the right bottom of the User
        if (Map.getSolid(newPosition.xIntPos() + 1, newPosition.yIntPos() + 1)) {
            tempVec = new Vector2D(newPosition.xIntPos() + 1, newPosition.yIntPos() + 1, newPosition.x, newPosition.y);
            l = tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.add(tempVec);
            }
        }
        // checks the wall to the right top of the User
        else if (Map.getSolid(newPosition.xIntPos() + 1, newPosition.yIntPos() - 1)) {
            tempVec = new Vector2D(newPosition.xIntPos() + 1, newPosition.yIntPos(), newPosition.x, newPosition.y);
            l = tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.add(tempVec);
            }
        }
        // checks the wall to the left top of the User
        else if (Map.getSolid(newPosition.xIntPos() - 1, newPosition.yIntPos() - 1)) {
            tempVec = new Vector2D(newPosition.xIntPos(), newPosition.yIntPos(), newPosition.x, newPosition.y);
            l = tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.add(tempVec);
            }
        }
        // checks the wall to the left bottom of the User
        else if (Map.getSolid(newPosition.xIntPos() - 1, newPosition.yIntPos() + 1)) {
            tempVec = new Vector2D(newPosition.xIntPos(), newPosition.yIntPos() + 1, newPosition.x, newPosition.y);
            l =  tempVec.getLength();
            if (l < playerRadius) {
                tempVec.scaleTo(playerRadius - l);
                newPosition.add(tempVec);
            }
        }
    }

    // if the User is on no more stable ground he starts to fall
    // the principle algorithm is quite the same as in the physics engine
    private void checkFloor(){
        // checks the floor below the user
        if (!Map.getFallThrough((int) (xCoordinate), (int) yCoordinate)) {
            return;
        }
        // checks the floor to the right of the User
        if (!Map.getFallThrough((int) (xCoordinate + playerRadius*FALL_DETECTION_TOLERANCE), (int) yCoordinate)) {
            return;
        }
        // checks the floor to the left of the User
        if (!Map.getFallThrough((int) (xCoordinate - playerRadius*FALL_DETECTION_TOLERANCE), (int) yCoordinate)) {
            return;
        }
        // checks the floor at the bottom of the User
        if (!Map.getFallThrough((int) xCoordinate, (int) (yCoordinate + playerRadius*FALL_DETECTION_TOLERANCE))) {
            return;
        }
        // checks the floor at the top of the User
        if (!Map.getFallThrough((int) xCoordinate, (int) (yCoordinate - playerRadius*FALL_DETECTION_TOLERANCE))) {
            return;
        }
        // checks the wall to the right bottom of the User
        if (!Map.getFallThrough(newPosition.xIntPos() + 1, newPosition.yIntPos() + 1) && Math.pow((int)xCoordinate - xCoordinate + 1, 2) + Math.pow((int)yCoordinate - yCoordinate + 1, 2) < playerRadius*FALL_DETECTION_TOLERANCE*playerRadius*FALL_DETECTION_TOLERANCE) {
            return;
        }
        // checks the wall to the right top of the User
        if (!Map.getFallThrough(newPosition.xIntPos() + 1, newPosition.yIntPos() - 1) && Math.pow((int)xCoordinate - xCoordinate + 1, 2) + Math.pow((int)yCoordinate - yCoordinate, 2) < playerRadius*FALL_DETECTION_TOLERANCE*playerRadius*FALL_DETECTION_TOLERANCE) {
            return;
        }
        // checks the wall to the left top of the User
        if (!Map.getFallThrough(newPosition.xIntPos() - 1, newPosition.yIntPos() - 1) && Math.pow((int)xCoordinate - xCoordinate, 2) + Math.pow((int)yCoordinate - yCoordinate, 2) < playerRadius*FALL_DETECTION_TOLERANCE*playerRadius*FALL_DETECTION_TOLERANCE) {
            return;
        }
        // checks the wall to the left bottom of the User
        if (!Map.getFallThrough(newPosition.xIntPos() - 1, newPosition.yIntPos() + 1) && Math.pow((int)xCoordinate - xCoordinate, 2) + Math.pow((int)yCoordinate - yCoordinate + 1, 2) < playerRadius*FALL_DETECTION_TOLERANCE*playerRadius*FALL_DETECTION_TOLERANCE) {
            return;
        }
        falling = true;
    }

    // if the ParticleButton is pressed and the User isn't stunned nor the Particles are on cool down then the User shoots a particle in the direction he is facing
    private void shoot(double shootAngle){
        ParticleShotEvent particleShotEvent = new ParticleShotEvent(xCoordinate, yCoordinate, shootAngle, GameThread.getCurrentFreeParticleID());
        particleShotEvent.send();
        particleShotEvent.apply();
        particleCoolDownTick = GameThread.getSynchronizedTick() + PARTICLE_COOL_DOWN;
    }

    private void shootRandom(){
        int y = 10;
        for(int i = 0; i < y; ++i) {
            shoot(- Math.PI + 2 * Math.PI * i / y);
        }
    }

    private void lightBulbInteraction(){
        // if already a request to get the LightBulb was sent there shouldn't be another one sent
        if (bulbRequestSent) {
            // if the LightBulb was received or picked up by someone else
            if (!pickUpBulb.isPickable()) {
                pickUpBulb = null;
                pickUpTickCount = 0;
                bulbRequestSent = false;
            }
        }
        // if the User is picking up a LightBulb
        else if (pickUpBulb != null) {
            pickUpTickCount++;
            // if the User is out of range or gets stunned he stops picking up
            if (stunned || Math.pow(xCoordinate - pickUpBulb.getXCoordinate(), 2) + Math.pow(yCoordinate - pickUpBulb.getYCoordinate(), 2) > PICK_UP_RANGE * PICK_UP_RANGE) {
                pickUpTickCount = 0;
                pickUpBulb = null;
            }
            // if the PICK_UP_TICKS have uninterruptedly passed the User asks the Server for the LightBulb
            else if (pickUpTickCount >= PICK_UP_TICKS) {
                if (StartActivity.deviceType == DeviceType.CLIENT) {
                    new LightBulbServerEvent(pickUpBulb.ID).send();
                }
                // if the User is the Server himself he does not have to ask for permission to take the LightBulb
                else {
                    new LightBulbEvent(pickUpBulb.ID).send();
                    bulbReceived(pickUpBulb.ID);
                }
                bulbRequestSent = true;
            }
        }
        // if the User isn't already in possession of a LightBulb and not currently picking up one
        // then he is allowed to start picking up one in range as long as it isn't possessed by an enemy or an allied LightBulbStand
        else if (lightBulb == null) {
            for (LightBulb lightBulb : GameThread.getLightBulbArray()) {
                if (lightBulb.getLightBulbStandTeam() == TEAM || !lightBulb.isPickable() || GameThread.getSynchronizedTick() < minLightBulbPickupTick) {
                    continue;
                }
                if (Math.pow(xCoordinate - lightBulb.getXCoordinate(), 2) + Math.pow(yCoordinate - lightBulb.getYCoordinate(), 2) <= PICK_UP_RANGE * PICK_UP_RANGE) {
                    pickUpTickCount = 0;
                    pickUpBulb = lightBulb;
                    break;
                }
            }
        }
        // if the request was sent and LightBulb put on the stand or failed the action a new event is allowed
        if (standRequestSent){
            if (!Map.getFriendlyLightBulbStands(TEAM)[requestedStandID].isFree()||lightBulb==null) {
                standRequestSent = false;
            }
        }
        // if the User is in possession of a LightBulb he checks for reachable allied LightBulbStands to put the LightBulb on
        //Also the User checks if he has lost all his strength. if so, he looses his LightBulb
        else if (lightBulb != null) {
            for (LightBulbStand lightBulbStand : Map.getFriendlyLightBulbStands(TEAM)) {
                if (lightBulbStand.isFree() && Math.pow(xCoordinate - lightBulbStand.CENTER_X, 2) + Math.pow(yCoordinate - lightBulbStand.CENTER_Y, 2) < PICK_UP_RANGE * PICK_UP_RANGE) {
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
            // if the User lost all his strength, he looses his LightBulb
            if (strength <= 0) {
                bulbLost();
            }
        }
    }

    // reducing the Players radius (called weight in method name)
    private void loseWeight(){
        float oldRadius = playerRadius;
        setPlayerRadius(getPlayerRadius() - RADIUS_CHANGE);
        // the radius is capped at a minimum
        if(getPlayerRadius() < MIN_RADIUS){
            setPlayerRadius(MIN_RADIUS);
        }
        if(playerRadius != oldRadius) new RadiusChangedEvent(getPlayerRadius()).send();
    }

    // the User's radius increases after eating a sweet
    public void eatSweet(Sweet sweet){
        setPlayerRadius(getPlayerRadius() + RADIUS_CHANGE);
        if(getPlayerRadius() > MAX_RADIUS){
            setPlayerRadius(MAX_RADIUS);
            exploding();
        }
        // handle collisions
        RadiusChangedEvent radiusChangedEvent = new RadiusChangedEvent(getPlayerRadius());
        radiusChangedEvent.send();
        SweetEatenEvent sweetEatenEvent = new SweetEatenEvent(sweet);
        sweetEatenEvent.send();
        sweetEatenEvent.apply();
    }

    // upon death the User has to reset his radius and inform the other devices
    protected void death(String deathReason) {
        DeathScreen.setDeathReason(deathReason);
        reviveTick = GameThread.getSynchronizedTick() + DEATH_TIME;
        new DeathEvent(true).send();
        new DeathEvent(true).apply();
        setPlayerRadius(MIN_RADIUS);
        new RadiusChangedEvent(MIN_RADIUS).send();
        bulbLost();
        mana = 0;
    }

    public void exploding(){
        death(" ATE TOO MUCH");
        shootRandom();
    }

    // used for a more simple call of the subclass skillActivation
    public void skillActivation(){

    }

    @Override
    public void bulbLost() {
        if (lightBulb != null) {
            new LightBulbEvent(lightBulb.ID, ID).send();
            minLightBulbPickupTick = GameThread.getSynchronizedTick() + Tick.TICK * 3;
            super.bulbLost();
        }
    }

    @Override
    public void bulbReceived(int bulbID) {
        super.bulbReceived(bulbID);
        bulbRequestSent = false;
    }

    public void setShooting(boolean isShooting){
        shooting = isShooting;
    }

    public void setVelocity(float velocity){
        this.velocity = velocity;
    }

    @Override
    public void setAngle(double angle) {
        angleChanged = true;
        super.setAngle(angle);
    }

    public double getAngle(){
        return angle;
    }

    public int getReviveTick() {
        return reviveTick;
    }

    public boolean getFalling(){
        return falling;
    }
}
