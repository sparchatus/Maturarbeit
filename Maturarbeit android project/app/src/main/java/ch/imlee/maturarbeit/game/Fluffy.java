package ch.imlee.maturarbeit.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 07.06.2015.
 */
public class Fluffy extends User{

    private Player focusedPlayer = null;
    private final Bitmap FOCUS_BMP;

    public Fluffy(float entityXCoordinate, float entityYCoordinate, PlayerType type, Map map, GameSurface.GameThread gameThread, int team) {
        super(entityXCoordinate, entityYCoordinate, type, map, gameThread, team);
        FOCUS_BMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameClient.getRec(), R.drawable.focus_overlay), PLAYER_SIDE, PLAYER_SIDE, false);
    }

    @Override
    public void update() {
        //do movement and if update the speed
        mana += speed / MAX_SPEED;
        if (mana >= MAX_MANA){
            mana = MAX_MANA;
        }
    }

    @Override
    public Canvas render(Canvas canvas) {
        canvas =  super.render(canvas);
        if (focusedPlayer != null){
            canvas.drawBitmap(FOCUS_BMP, (focusedPlayer.getXCoordinate() - xCoordinate - PLAYER_RADIUS) *    PLAYER_SIDE + GameClient.getHalveScreenWidth(), (focusedPlayer.getYCoordinate() - yCoordinate - PLAYER_RADIUS) * PLAYER_SIDE + GameClient.getHalveScreenHeight(), null);
        }
        return canvas;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (Player player : gameThread.getPlayerArray()) {
                if (Math.sqrt(Math.pow((event.getX() - GameClient.getHalveScreenWidth()) / PLAYER_SIDE + user.getXCoordinate() - player.getXCoordinate(), 2) + Math.pow((event.getY() - GameClient.getHalveScreenHeight()) / PLAYER_SIDE + user.getYCoordinate() - player.getYCoordinate(), 2)) < player.PLAYER_RADIUS) {
                    if (player.TEAM != user.TEAM) {
                        focusedPlayer = player;
                        return false;
                    }
                }
            }
        }
        return super.onTouch(event);
    }

    @Override
    public boolean skillActivation() {
        if (super.skillActivation() && focusedPlayer != null){
            focusedPlayer.stun();
            focusedPlayer = null;
            //send the stun event
            return true;
        }
        return false;
    }
}
