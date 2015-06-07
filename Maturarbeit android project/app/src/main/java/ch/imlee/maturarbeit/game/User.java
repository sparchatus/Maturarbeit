package ch.imlee.maturarbeit.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 06.06.2015.
 */
public class User extends Player{

    private final Paint SKILL_BAR_COLOR;
    protected final int MAX_MANA = 1000;
    protected final float MAX_SPEED = 0.2f;

    protected float mana;
    protected float speed;

    public User(float entityXCoordinate, float entityYCoordinate, PlayerType type, Map map, GameSurface.GameThread gameThread, int team) {
        super(entityXCoordinate, entityYCoordinate, type, map, gameThread, team);
        SKILL_BAR_COLOR = new Paint();
        if (type == PlayerType.FLUFFY){
            SKILL_BAR_COLOR.setColor(0xff0000ff);
        }else if (type == PlayerType.SLIME){
            SKILL_BAR_COLOR.setColor(0xff00ff00);
        }else if (type == PlayerType.GHOST){
            SKILL_BAR_COLOR.setColor(0xff000000);
        }
    }

    @Override
    public void update() {
        super.update();
        move();
    }

    @Override
    public Canvas render(Canvas canvas){
        //draw player at right angle
        Matrix matrix = new Matrix();
        matrix.postRotate((float) (angle / 2 / Math.PI * 360) - 90);
        Bitmap rotated = Bitmap.createBitmap(PLAYER_BMP, 0, 0, PLAYER_BMP.getWidth(), PLAYER_BMP.getHeight(), matrix, true);
        canvas.drawBitmap(rotated, GameClient.getHalveScreenWidth() - rotated.getWidth() / 2, GameClient.getHalveScreenHeight() - rotated.getHeight() / 2, null);
        //draw mana bar
        canvas.drawRect(0, GameClient.getHalveScreenHeight() * 2 - BAR_HEIGHT, GameClient.getHalveScreenHeight() * 2, GameClient.getHalveScreenHeight() * 2, BAR_BACKGROUND_COLOR);
        canvas.drawRect(0, GameClient.getHalveScreenHeight() * 2 - BAR_HEIGHT, GameClient.getHalveScreenHeight() * 2 * mana / MAX_MANA, GameClient.getHalveScreenHeight() * 2, SKILL_BAR_COLOR);
        return  canvas;
    }

    private void move() {
        if (stunned)return;
        xCoordinate += Math.cos(angle) * speed * MAX_SPEED;
        yCoordinate += Math.sin(angle) * speed * MAX_SPEED;
    }
    public boolean onTouch(MotionEvent event){
        float distance = (float) Math.sqrt(Math.pow(event.getX() - GameClient.getHalveScreenWidth(), 2) + Math.pow(event.getY() - GameClient.getHalveScreenHeight(), 2));
        float angle = (float) Math.acos((event.getX() - GameClient.getHalveScreenWidth()) / distance);
        if(event.getY() - GameClient.getHalveScreenHeight() < 0){
            angle *= -1;
        }
        if (distance <= user.PLAYER_RADIUS * PLAYER_SIDE){
            speed = 0;
            return true;
        }else {
            float userSpeed = distance / GameClient.getHalveScreenHeight();
            if (speed > 1) {
                speed = 1;
            }
            speed = userSpeed;
        }
        user.setAngle(angle);
        return true;
    }

    public boolean skillActivation(){
        if (mana >= MAX_MANA){
            mana = 0;
            return true;
        }
        return false;
    }
}
