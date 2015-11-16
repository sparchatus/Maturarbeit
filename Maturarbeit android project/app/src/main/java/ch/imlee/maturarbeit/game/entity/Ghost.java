package ch.imlee.maturarbeit.game.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.events.gameActionEvents.InvisibilityEvent;
import ch.imlee.maturarbeit.views.GameSurface;

public class Ghost extends User {

    private final int MANA_CONSUMPTION = MAX_MANA / 100;

    private final Bitmap INVISIBLE_GHOST;
    private Bitmap scaledInvisibleGhostBmp;

    public Ghost(Map map, byte team, byte playerId, String name) {
        super(PlayerType.GHOST, map, team, playerId, name);
        INVISIBLE_GHOST = Bitmap.createBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.ghost_invisible));
        scaledInvisibleGhostBmp = Bitmap.createScaledBitmap(INVISIBLE_GHOST, Map.TILE_SIDE, Map.TILE_SIDE, false);
    }

    @Override
    public void update() {
        super.update();
        // Ghost degenerates mana when his skill is active
        if (invisible){
            if (mana <= 0){
                setInvisible(false);
            }else {
                mana -= MANA_CONSUMPTION;
            }
        }
        mana += MANA_CONSUMPTION / 5;
        // the mana is capped at MAX_MANA
        if (mana >= MAX_MANA){
            mana = MAX_MANA;
        }
    }

    // the Objects are generally drawn in relation to the User position on the Map because the User's position on the screen is constant
    @Override
    public Canvas render(Canvas canvas) {
        canvas = super.render(canvas);
        // the Ghost gets rendered differently (with another Bitmap)  when his skill is active
        if (invisible){
            Matrix matrix = new Matrix();
            matrix.postRotate((float) (angle / Math.PI * 180) - 90);
            Bitmap rotated = Bitmap.createBitmap(scaledInvisibleGhostBmp, 0, 0, scaledInvisibleGhostBmp.getWidth(), scaledInvisibleGhostBmp.getHeight(), matrix, true);
            canvas.drawBitmap(rotated, (xCoordinate - user.getXCoordinate()) * Map.TILE_SIDE + GameSurface.getSurfaceWidth() / 2 - rotated.getWidth() / 2, (yCoordinate - user.getYCoordinate()) * Map.TILE_SIDE + GameSurface.getSurfaceHeight() / 2 - rotated.getHeight() / 2, null);
        }
        return canvas;
    }

    // Ghost has a toggle skill
    @Override
    public void skillActivation() {
        if(invisible){
            setInvisible(false);
        }else{
            setInvisible(true);
        }
    }

    // the stealth Bitmap also has to change its size
    @Override
    public void setPlayerRadius(float radius) {
        super.setPlayerRadius(radius);
        scaledInvisibleGhostBmp = Bitmap.createScaledBitmap(INVISIBLE_GHOST, (int) (radius * 2 * Map.TILE_SIDE), (int) (radius * 2 * Map.TILE_SIDE), false);
    }

    // if the visibility of the Ghost is changed, the other devices need to be informed
    @Override
    public void setInvisible(boolean invisible){
        this.invisible = invisible;
        new InvisibilityEvent(ID, invisible).send();
    }
}
