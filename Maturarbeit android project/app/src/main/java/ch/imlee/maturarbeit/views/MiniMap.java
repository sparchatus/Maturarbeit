package ch.imlee.maturarbeit.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.special_screens.EndGameScreen;

public class MiniMap extends SurfaceView implements SurfaceHolder.Callback {

    // the MiniMap is by default small
    private static boolean isSmall = true;

    // different transparencies
    private static final int SMALL_ALPHA = 0xcc;
    private static final int BIG_ALPHA = 0xee;
    // some violet/pink color
    private static int USER_COLOR = 0xffff00ff;
    private static int ALLY_COLOR;
    private static int ENEMY_COLOR;
    //only required for the setup
    private static int width;
    private static int height;

    // these are constants and should never change once they are assigned!
    // they aren't final because they can't be assigned inside the constructor
    private static int SMALL_MINI_MAP_ORIGIN_X;
    private static int SMALL_MINI_MAP_ORIGIN_Y = 0;
    private static int BIG_MINI_MAP_ORIGIN_X;
    private static int BIG_MINI_MAP_ORIGIN_Y = 0;
    private static float TILE_SIDE_SMALL_MINI_MAP;
    private static float TILE_SIDE_BIG_MINI_MAP;
    private static Bitmap SMALL_MINI_MAP;
    private static Bitmap BIG_MINI_MAP;
    private static Bitmap CROSS;
    //end of the constants

    private static SurfaceHolder holder;
    private static Paint miniMapPaint;
    private static Paint miniMapPlayerPaint;

    public MiniMap(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        miniMapPaint = new Paint();
        // the MiniMap is by default small
        miniMapPaint.setAlpha(SMALL_ALPHA);
        miniMapPlayerPaint = new Paint();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // needs to be called to get the real width and height
        invalidate();
        width = getWidth();
        height = getHeight();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // needs to be called to get the real width and height
        invalidate();
        this.width = getWidth();
        this.height = getHeight();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public static void setup() {
        // the TILE_SIDE variables are required to determine the size of the Players on the MiniMap
        TILE_SIDE_SMALL_MINI_MAP = width / 1f / Map.TILES_IN_MAP_WIDTH;
        TILE_SIDE_BIG_MINI_MAP = GameSurface.getSurfaceHeight() / 1f / Map.TILES_IN_MAP_WIDTH;
        // to avoid rescaling both sizes are loaded at the start
        SMALL_MINI_MAP = Bitmap.createScaledBitmap(Map.getPixelMap(), width, height, false);
        BIG_MINI_MAP = Bitmap.createScaledBitmap(Map.getPixelMap(), GameSurface.getSurfaceHeight(), GameSurface.getSurfaceHeight(), false);
        CROSS = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.cross), width, height, false);
        if (GameThread.getUser().TEAM == 0) {
            // blue
            ALLY_COLOR = 0xff0000ff;
            // green
            ENEMY_COLOR = 0xff00ff00;
        } else {
            // green
            ALLY_COLOR = 0xff00ff00;
            // blue
            ENEMY_COLOR = 0xff0000ff;
        }
        SMALL_MINI_MAP_ORIGIN_X = GameSurface.getSurfaceWidth() - width;
        BIG_MINI_MAP_ORIGIN_X = (GameSurface.getSurfaceWidth() - GameSurface.getSurfaceHeight()) / 2;
    }

    public void render(Canvas canvas) {
        if (isSmall) {
            renderSmall(canvas);
        } else {
            renderBig(canvas);
        }
    }

    private void renderSmall(Canvas canvas){
        // first the pixel map image (pixel file)is drawn
        canvas.drawBitmap(SMALL_MINI_MAP, SMALL_MINI_MAP_ORIGIN_X, SMALL_MINI_MAP_ORIGIN_Y, miniMapPaint);
        for (Player player : GameThread.getPlayerArray()) {
            // the paint is set according to the player's team (except for the User of course) which is the the only thing making a difference on the MiniMap
            if (player.getID() == GameThread.getUser().getID()) {
                miniMapPlayerPaint.setColor(USER_COLOR);
            } else if (player.TEAM == GameThread.getUser().TEAM) {
                miniMapPlayerPaint.setColor(ALLY_COLOR);
            } else {
                if (player.getInvisible()) break;
                miniMapPlayerPaint.setColor(ENEMY_COLOR);
            }
            canvas.drawCircle(SMALL_MINI_MAP_ORIGIN_X + player.getXCoordinate() * TILE_SIDE_SMALL_MINI_MAP, SMALL_MINI_MAP_ORIGIN_Y + player.getYCoordinate() * TILE_SIDE_SMALL_MINI_MAP, player.getPlayerRadius() * TILE_SIDE_SMALL_MINI_MAP, miniMapPlayerPaint);            }
    }

    private void renderBig(Canvas canvas){
        // first the pixel map image (pixel file)is drawn
        canvas.drawBitmap(BIG_MINI_MAP, BIG_MINI_MAP_ORIGIN_X, BIG_MINI_MAP_ORIGIN_Y, miniMapPaint);
        // the cross is rendered where the small MiniMap would be
        canvas.drawBitmap(CROSS, SMALL_MINI_MAP_ORIGIN_X, SMALL_MINI_MAP_ORIGIN_Y, null);
        for (Player player : GameThread.getPlayerArray()) {
            // the paint is set according to the player's team (except for the User of course) which is the the only thing making a difference on the MiniMap
            if (player.getID() == GameThread.getUser().getID()) {
                miniMapPlayerPaint.setColor(USER_COLOR);
            } else if (player.TEAM == GameThread.getUser().TEAM) {
                miniMapPlayerPaint.setColor(ALLY_COLOR);
            } else {
                if (player.getInvisible()) break;
                miniMapPlayerPaint.setColor(ENEMY_COLOR);
            }
            canvas.drawCircle(BIG_MINI_MAP_ORIGIN_X + player.getXCoordinate() * TILE_SIDE_BIG_MINI_MAP, BIG_MINI_MAP_ORIGIN_Y + player.getYCoordinate() * TILE_SIDE_BIG_MINI_MAP, player.getPlayerRadius() * TILE_SIDE_BIG_MINI_MAP, miniMapPlayerPaint);
        }
    }

    public void makeBig() {
        isSmall = false;
        miniMapPlayerPaint.setAlpha(BIG_ALPHA);
        miniMapPaint.setAlpha(BIG_ALPHA);
    }

    public void makeSmall() {
        isSmall = true;
        miniMapPlayerPaint.setAlpha(SMALL_ALPHA);
        miniMapPaint.setAlpha(SMALL_ALPHA);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (GameThread.getEndGameActive()){
            return EndGameScreen.onTouch(event);
        }
        // before the main loop finished loading there might be some variables missing which can lead to an error
        if (GameThread.getLoading()) {
            return false;
        }
        if (isSmall) {
            makeBig();
        } else {
            makeSmall();
        }
        return false;
    }
}