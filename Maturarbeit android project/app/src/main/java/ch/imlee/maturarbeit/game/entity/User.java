package ch.imlee.maturarbeit.game.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.events.gameActionEvents.PlayerMotionEvent;
import ch.imlee.maturarbeit.game.views.GameSurface;

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
    protected float speed;
     protected float realSpeed;

     protected Map map;

    public User(float entityXCoordinate, float entityYCoordinate, PlayerType type, Map map, GameSurface.GameThread gameThread, byte team, byte playerId, User theUser) {
        super(entityXCoordinate, entityYCoordinate, type, map, gameThread, team, playerId, theUser);
        user = this;
        SKILL_BAR_COLOR = new Paint();
        if (type == PlayerType.FLUFFY){
            SKILL_BAR_COLOR.setColor(0xff0000ff);
        }else if (type == PlayerType.SLIME){
            SKILL_BAR_COLOR.setColor(0xff00ff00);
        }else if (type == PlayerType.GHOST){
            SKILL_BAR_COLOR.setColor(0xff000000);
        }
        this.map = map;
    }

    @Override
    public void update() {
        super.update();
        move();
        if (shooting && particleCoolDownTick <= gameThread.getSynchronizedTick()){
            gameThread.addParticle(new Particle(this, gameThread));
            particleCoolDownTick = gameThread.getSynchronizedTick() + PARTICLE_COOL_DOWN;
        }
    }

    @Override
    public Canvas render(Canvas canvas){
        canvas = super.render(canvas);
        //draw mana bar
        canvas.drawRect(0, GameClient.getHalfScreenHeight() * 2 - BAR_HEIGHT, GameClient.getHalfScreenWidth() * 2, GameClient.getHalfScreenHeight() * 2, BAR_BACKGROUND_COLOR);
        canvas.drawRect(0, GameClient.getHalfScreenHeight() * 2 - BAR_HEIGHT, GameClient.getHalfScreenWidth() * 2 * mana / MAX_MANA, GameClient.getHalfScreenHeight() * 2, SKILL_BAR_COLOR);
        return  canvas;
    }

    private void move() {
        if (stunned || speed == 0) {
            realSpeed = 0;
            return;
        }
        float newXCoordinate = (float) (xCoordinate + Math.cos(angle) * speed * MAX_SPEED);
        float newYCoordinate = (float) (yCoordinate + Math.sin(angle) * speed * MAX_SPEED);
        if(map.getSolid((int)(newXCoordinate - 0.5), (int)newYCoordinate) || map.getSolid((int)(newXCoordinate + 0.5), (int)newYCoordinate)){
            newXCoordinate = (int)newXCoordinate + 0.5f;
        }
        if(map.getSolid((int)newXCoordinate, (int)(newYCoordinate - 0.5)) || map.getSolid((int)newXCoordinate, (int)(newYCoordinate + 0.5))){
            newYCoordinate = (int)newYCoordinate + 0.5f;
        }
        realSpeed = (float) Math.sqrt(Math.pow((newXCoordinate - xCoordinate) / MAX_SPEED, 2) + Math.pow((newYCoordinate - yCoordinate) / MAX_SPEED, 2));
        xCoordinate = newXCoordinate;
        yCoordinate = newYCoordinate;
        new PlayerMotionEvent(xCoordinate, yCoordinate, angle, ID).send();
    }

    public boolean onTouch(MotionEvent event){
        float distance = (float) Math.sqrt(Math.pow(event.getX() - GameClient.getHalfScreenWidth(), 2) + Math.pow(event.getY() - GameClient.getHalfScreenHeight(), 2));
        float newAngle = (float) Math.acos((event.getX() - GameClient.getHalfScreenWidth()) / distance);
        if(event.getY() - GameClient.getHalfScreenHeight() < 0){
            newAngle *= -1;
        }
        if (distance <= user.PLAYER_RADIUS * PLAYER_SIDE){
            speed = 0;
            return true;
        }else {
            float newSpeed = distance / GameClient.getHalfScreenHeight();
            if (newSpeed > 1) {
                newSpeed = 1;
            }
            speed = newSpeed;
        }
        angle = newAngle;
        return true;
    }

    public void skillActivation(){

    }

    public void setShooting(boolean isShooting){
        shooting = isShooting;
    }
}
