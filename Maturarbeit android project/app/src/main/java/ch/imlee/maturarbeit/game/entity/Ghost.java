package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.Map;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 11.06.2015.
 */
public class Ghost extends User {

    private final Bitmap INVISIBLE_GHOST;
    private int MANA_CONSUMPTION = MAX_MANA / 100;


    public Ghost(float entityXCoordinate, float entityYCoordinate, PlayerType type, Map map, GameSurface.GameThread gameThread, byte team, byte playerId, User theUser) {
        super(entityXCoordinate, entityYCoordinate, type, map, gameThread, team, playerId, theUser);
        INVISIBLE_GHOST = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameClient.getRec(), R.drawable.ghost_invisible), PLAYER_SIDE, PLAYER_SIDE, false);
    }

    @Override
    public void update() {
        super.update();
        if (invisible){
            if (mana <= 0){
                invisible = false;
            }else {
                mana -= MANA_CONSUMPTION;
            }
        }
        mana += 2;
    }

    @Override
    public Canvas render(Canvas canvas) {
      if (invisible){
          Matrix matrix = new Matrix();
          matrix.postRotate((float) (angle / 2 / Math.PI * 360) - 90);
          Bitmap rotated = Bitmap.createBitmap(INVISIBLE_GHOST, 0, 0, INVISIBLE_GHOST.getWidth(), INVISIBLE_GHOST.getHeight(), matrix, true);
          canvas.drawBitmap(rotated, (xCoordinate - user.getXCoordinate()) * PLAYER_SIDE + GameClient.getHalveScreenWidth() - rotated.getWidth() / 2, (yCoordinate - user.getYCoordinate()) * PLAYER_SIDE + GameClient.getHalveScreenHeight() - rotated.getHeight() / 2, null);
          canvas.drawRect(0, GameClient.getHalveScreenHeight() * 2 - BAR_HEIGHT, GameClient.getHalveScreenWidth() * 2, GameClient.getHalveScreenHeight() * 2, BAR_BACKGROUND_COLOR);
          canvas.drawRect(0, GameClient.getHalveScreenHeight() * 2 - BAR_HEIGHT, GameClient.getHalveScreenWidth() * 2 * mana / MAX_MANA, GameClient.getHalveScreenHeight() * 2, SKILL_BAR_COLOR);
          if (stunned){
              canvas.drawBitmap(STUN_BMP, (xCoordinate - user.getXCoordinate() - PLAYER_RADIUS) * PLAYER_SIDE + GameClient.getHalveScreenWidth(), (yCoordinate - user.getYCoordinate() - PLAYER_RADIUS) * PLAYER_SIDE + GameClient.getHalveScreenHeight(), null);
          }
      } else{
          canvas = super.render(canvas);
      }
        return canvas;
    }

    @Override
    public void skillActivation() {
        if(invisible){
            invisible = false;
        }else {
            invisible = true;
        }
    }
}
