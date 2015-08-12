package ch.imlee.maturarbeit.game.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.events.gameActionEvents.ParticleShotEvent;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.events.gameActionEvents.PlayerMotionEvent;

 /**
 * Created by Sandro on 06.06.2015.
 */
public class User extends Player {

    protected final Paint SKILL_BAR_COLOR;
    protected final int MAX_MANA = 1000;
    protected final float MAX_SPEED = 0.2f;
    protected final int PARTICLE_COOL_DOWN = 300 / TIME_PER_TICK;

    protected boolean shooting;

    protected double particleCoolDownTick;

    protected float mana;
     //velocity determines how the far the player wants to travel in the next update and speed is the distance it travelled in the last update, angle os the angle from the last update
     protected float velocity, speed, oldAngle;
     protected Map map;

    public User(float entityXCoordinate, float entityYCoordinate, PlayerType type, Map map, byte team, byte playerId, User theUser) {
        super(entityXCoordinate, entityYCoordinate, type, map, team, playerId, theUser);
        user = this;
        SKILL_BAR_COLOR = new Paint();
        if (type == PlayerType.FLUFFY){
            SKILL_BAR_COLOR.setColor(0xff0000ff);
        }else if (type == PlayerType.GHOST){
            SKILL_BAR_COLOR.setColor(0xff000000);
        }else if (type == PlayerType.SLIME){
            SKILL_BAR_COLOR.setColor(0xff00ff00);
        }
        this.map = map;
    }

    @Override
    public void update() {
        super.update();
        move();
        if (shooting && particleCoolDownTick <= GameThread.getSynchronizedTick()){
            GameThread.addParticle(new Particle(this));
            new ParticleShotEvent(this.getID()).send();
            particleCoolDownTick = GameThread.getSynchronizedTick() + PARTICLE_COOL_DOWN;
        }
    }

    @Override
    public Canvas render(Canvas canvas){
        canvas = super.render(canvas);
        canvas.drawRect(0, GameClient.getHalfScreenHeight() * 2 - BAR_HEIGHT, GameClient.getHalfScreenWidth() * 2, GameClient.getHalfScreenHeight() * 2, BAR_BACKGROUND_COLOR);
        canvas.drawRect(0, GameClient.getHalfScreenHeight() * 2 - BAR_HEIGHT, GameClient.getHalfScreenWidth() * 2 * mana / MAX_MANA, GameClient.getHalfScreenHeight() * 2, SKILL_BAR_COLOR);
        return  canvas;
    }

    private void move() {
        //TODO better hit boxes with walls
        //TODO players can fall out of the world
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
            new PlayerMotionEvent(xCoordinate, yCoordinate, angle, ID).send();
        }
    }

    public void skillActivation(){

    }

    public void setShooting(boolean isShooting){
        shooting = isShooting;
    }

     public void setVelocity(float velocity){
         this.velocity = velocity;
     }
     public float getPLAYER_RADIUS(){
         return PLAYER_RADIUS;
     }
}
