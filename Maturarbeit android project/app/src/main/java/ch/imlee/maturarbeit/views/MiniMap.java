package ch.imlee.maturarbeit.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.activities.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.map.MapDimensions;

/**
 * Created by Sandro on 09.09.2015.
 */
public class MiniMap extends SurfaceView implements SurfaceHolder.Callback, MapDimensions {

    private static int USER_COLOR = 0xffffff00;
    private static int ALLY_COLOR = 0xff0000ff;
    private static int ENEMY_COLOR = 0xff00ff00;
    private static SurfaceHolder holder;
    private static int width;
    private static int height;
    private static int gameSurfaceHeight;
    private static Paint miniMapPaint;
    private static Bitmap SMALL_MINI_MAP;
    private static Bitmap BIG_MINI_MAP;
    private static Bitmap CROSS;
    private static final int SMALL_ALPHA = 0xaa;
    private static final int BIG_ALPHA = 0xd0;
    private static int smallPlayerRadius, bigPlayerRadius;
    private static Paint miniMapPlayerPaint;
    private static boolean isSmall;
    private static float TILE_SIDE_SMALL_MINI_MAP;
    private static float TILE_SIDE_BIG_MINI_MAP;
    private static int SMALL_MINI_MAP_ORIGIN_X;
    private static int SMALL_MINI_MAP_ORIGIN_Y = 0;
    private static int BIG_MINI_MAP_ORIGIN_X;
    private static int BIG_MINI_MAP_ORIGIN_Y = 0;
    private static Resources resources;

    public MiniMap(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        miniMapPaint = new Paint();
        miniMapPaint.setAlpha(SMALL_ALPHA);
        miniMapPlayerPaint = new Paint();
        miniMapPlayerPaint.setAlpha(SMALL_ALPHA);
    }

    public static void setup() {
        isSmall = true;
        TILE_SIDE_SMALL_MINI_MAP = width / Map.TILES_IN_SCREEN_WIDTH;
        TILE_SIDE_BIG_MINI_MAP = gameSurfaceHeight / Map.TILES_IN_SCREEN_WIDTH;
        gameSurfaceHeight = GameSurface.getSurfaceHeight();
        SMALL_MINI_MAP = Bitmap.createScaledBitmap(Map.getPixelMap(), width, height, false);
        BIG_MINI_MAP = Bitmap.createScaledBitmap(Map.getPixelMap(), gameSurfaceHeight, gameSurfaceHeight, false);
        CROSS = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.cross), width, height, false);
        if (GameThread.getUser().TEAM == 0) {
            ALLY_COLOR = 0xff0000ff;
            ENEMY_COLOR = 0xff00ff00;
        } else {
            ALLY_COLOR = 0xff00ff00;
            ENEMY_COLOR = 0xff0000ff;
        }
        SMALL_MINI_MAP_ORIGIN_X = GameSurface.getSurfaceWidth() - width;
        BIG_MINI_MAP_ORIGIN_X = (GameSurface.getSurfaceWidth() - gameSurfaceHeight) / 2;
        smallPlayerRadius = (int) (GameThread.getUser().getPlayerRadius() * TILE_SIDE_SMALL_MINI_MAP);
        bigPlayerRadius = (int) (GameThread.getUser().getPlayerRadius() * TILE_SIDE_BIG_MINI_MAP);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("MiniMapSurface", "Surface created.");
        invalidate();
        width = getWidth();
        height = getHeight();
        resources = getResources();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("MiniMapSurface", "Surface changed.");
        invalidate();
        width = getWidth();
        height = getHeight();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("MiniMapSurface", "Surface destroyed.");
    }

    public void render(Canvas canvas) {
        //Todo:make it look better, ad player render and touch event
        User user = GameThread.getUser();
        if (isSmall) {
            canvas.drawBitmap(SMALL_MINI_MAP, SMALL_MINI_MAP_ORIGIN_X, SMALL_MINI_MAP_ORIGIN_Y, miniMapPaint);
            for (Player player : GameThread.getPlayerArray()) {
                if (player.getID() == user.getID()) {
                    miniMapPlayerPaint.setColor(USER_COLOR);
                } else if (player.TEAM == user.TEAM) {
                    miniMapPlayerPaint.setColor(ALLY_COLOR);
                } else {
                    if (player.getInvisible()) break;
                    miniMapPlayerPaint.setColor(ENEMY_COLOR);
                }
                canvas.drawCircle(SMALL_MINI_MAP_ORIGIN_X + player.getXCoordinate() * TILE_SIDE_SMALL_MINI_MAP, SMALL_MINI_MAP_ORIGIN_Y + player.getYCoordinate() * TILE_SIDE_SMALL_MINI_MAP, smallPlayerRadius, miniMapPlayerPaint);
            }
        } else {
            canvas.drawBitmap(BIG_MINI_MAP, BIG_MINI_MAP_ORIGIN_X, BIG_MINI_MAP_ORIGIN_Y, miniMapPaint);
            canvas.drawBitmap(CROSS, SMALL_MINI_MAP_ORIGIN_X, SMALL_MINI_MAP_ORIGIN_Y, null);
            for (Player player : GameThread.getPlayerArray()) {
                if (player.getID() == user.getID()) {
                    miniMapPlayerPaint.setColor(USER_COLOR);
                } else if (player.TEAM == user.TEAM) {
                    miniMapPlayerPaint.setColor(ALLY_COLOR);
                } else {
                    if (player.getInvisible()) break;
                    miniMapPlayerPaint.setColor(ENEMY_COLOR);
                }
                canvas.drawCircle(BIG_MINI_MAP_ORIGIN_X + player.getXCoordinate() * TILE_SIDE_BIG_MINI_MAP, BIG_MINI_MAP_ORIGIN_Y + player.getYCoordinate() * TILE_SIDE_BIG_MINI_MAP, bigPlayerRadius, miniMapPlayerPaint);
            }
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