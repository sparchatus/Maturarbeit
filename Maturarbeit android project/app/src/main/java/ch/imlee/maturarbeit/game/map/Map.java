package ch.imlee.maturarbeit.game.map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.views.GameSurface;

/**
 * Created by Sandro on 04.06.2015.
 */
public class Map implements MapDimensions {
    public static int TILE_SIDE;
    private final int MINIMAP_WIDTH = GameSurface.getSurfaceWidth()/4;
    private final int MINIMAP_HEIGHT = (int)(((float)MINIMAP_WIDTH/(float)(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2).getWidth()-2*BORDER_TILES_RIGHT))*
            (float)(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2).getHeight()-2*BORDER_TILES_TOP));
    private final Bitmap MINIMAP_ORIGINAL_BITMAP = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2);
    private final Bitmap MINIMAP_BITMAP = Bitmap.createScaledBitmap(Bitmap.createBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2),
            BORDER_TILES_RIGHT-1, BORDER_TILES_TOP-1, BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2).getWidth()-2*(BORDER_TILES_RIGHT-1),
            BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2).getHeight()-2*(BORDER_TILES_TOP-1)), MINIMAP_WIDTH, MINIMAP_HEIGHT, false);
    final float MINIMAP_SCALE = MINIMAP_BITMAP.getWidth()/(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2).getWidth()-2*BORDER_TILES_RIGHT);
    private static Paint minimapPaint = new Paint();
    private static final int MINIMAP_ALPHA = 0xaa;
    private static float[][] playerStartCoordinates = new float[8][2];
    private static int blueCoordinateDistributionIndex;
    private static int greenCoordinateDistributionIndex;
    private final Tile[][]TILE_MAP;
    private Tile voidTile;
    private Tile groundTile;
    private Tile wallTile;
    private Tile greenBaseTile;
    private Tile blueBaseTile;
    private Tile spawnTile;
    private static LightBulbStand[] blueLightBulbStandArray = new LightBulbStand[2];
    private static LightBulbStand[] greenLightBulbStandArray = new LightBulbStand[2];

    public Map(Resources rec, int pixelMapID) {
        minimapPaint.setAlpha(0xCC);
        TILE_SIDE = GameSurface.getSurfaceHeight() / TILES_IN_SCREEN_HEIGHT;
        Bitmap pixelMap = BitmapFactory.decodeResource(rec, pixelMapID);
        voidTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.void_tile), TILE_SIDE, TILE_SIDE, false), false);
        groundTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.ground_tile), TILE_SIDE, TILE_SIDE, false), false);
        wallTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.wall_tile), TILE_SIDE, TILE_SIDE, false), true);
        greenBaseTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.green_base_tile), TILE_SIDE, TILE_SIDE, false), true);
        blueBaseTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.blue_base_tile), TILE_SIDE, TILE_SIDE, false), true);
        spawnTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.spawn_tile), TILE_SIDE, TILE_SIDE, false), false);
        Bitmap blueLightBulbTileBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.light_bulb_tile_team_blue), TILE_SIDE, TILE_SIDE, false);
        Bitmap greenLightBulbTileBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.light_bulb_tile_team_green), TILE_SIDE, TILE_SIDE, false);
        TILE_MAP = new Tile[pixelMap.getWidth()][pixelMap.getHeight()];
        int blueLightBulbStandDistributionIndex = 0;
        int greenLightBulbStandDistributionIndex = 0;
        for(int y = 0; y < pixelMap.getHeight(); y++){
            for (int x = 0; x < pixelMap.getWidth(); x++){
                if(pixelMap.getPixel(x, y) == 0xffffffff) {
                    TILE_MAP[x][y] = groundTile;
                }else if(pixelMap.getPixel(x, y) == 0xffff0000) {
                    TILE_MAP[x][y] = wallTile;
                }else if (pixelMap.getPixel(x, y) == 0xff00ff00){
                    TILE_MAP[x][y] = greenBaseTile;
                }else if (pixelMap.getPixel(x, y) == 0xff0000ff){
                    TILE_MAP[x][y] = blueBaseTile;
                }else if (pixelMap.getPixel(x, y) == 0xff00ffff){
                    TILE_MAP[x][y] = spawnTile;
                    playerStartCoordinates[blueCoordinateDistributionIndex][0] = x + 0.5f;
                    playerStartCoordinates[blueCoordinateDistributionIndex][1] = y + 0.5f;
                    blueCoordinateDistributionIndex++;
                }else if (pixelMap.getPixel(x, y) == 0xff01ffff){
                    TILE_MAP[x][y] = spawnTile;
                    playerStartCoordinates[greenCoordinateDistributionIndex + 4][0] = x + 0.5f;
                    playerStartCoordinates[greenCoordinateDistributionIndex + 4][1] = y + 0.5f;
                    greenCoordinateDistributionIndex++;
                }else if (pixelMap.getPixel(x, y) == 0xffffff00){
                    TILE_MAP[x][y] = new LightBulbStand(x, y, blueLightBulbTileBmp, true, (byte) 0);
                    blueLightBulbStandArray[blueLightBulbStandDistributionIndex] = (LightBulbStand)TILE_MAP[x][y];
                    blueLightBulbStandDistributionIndex++;
                }else if (pixelMap.getPixel(x, y) == 0xffffff01){
                    TILE_MAP[x][y] = new LightBulbStand(x, y, greenLightBulbTileBmp, true, (byte) 1);
                    greenLightBulbStandArray[greenLightBulbStandDistributionIndex] = (LightBulbStand) TILE_MAP[x][y];
                    greenLightBulbStandDistributionIndex++;
                }else{
                    TILE_MAP[x][y] = voidTile;
                }
            }
        }
        //minus one because the ++ in the coordinate distribution function has to be before the return
        blueCoordinateDistributionIndex = -1;
        greenCoordinateDistributionIndex = -1;
    }

    public void render(Canvas canvas, User user){
        for (int i = - BORDER_TILES_TOP; i <= BORDER_TILES_TOP; i++){
            for (int j = - BORDER_TILES_RIGHT; j <= BORDER_TILES_RIGHT; j++){
                canvas.drawBitmap(TILE_MAP[((int) user.getXCoordinate()) + j][((int) user.getYCoordinate()) + i].BMP, GameSurface.getSurfaceWidth() / 2 + (((int) user.getXCoordinate()) + j - user.getXCoordinate()) * TILE_SIDE, GameSurface.getSurfaceHeight() / 2+ (((int) user.getYCoordinate()) + i - user.getYCoordinate())* TILE_SIDE, null);
            }
        }
    }
    public void renderMinimap(Canvas canvas){
        minimapPaint.setAlpha(MINIMAP_ALPHA);
        canvas.drawBitmap(MINIMAP_BITMAP, GameSurface.getSurfaceWidth()-MINIMAP_WIDTH, 0, minimapPaint);

        for(byte i = 0; i < GameThread.getPlayerArray().length; ++i){
            if(i==GameThread.getUser().getID()){
                minimapPaint.setColor(0x0000ff);
            } else if(GameThread.getPlayerArray()[i].getTeam()== GameThread.getPlayerArray()[GameThread.getUser().getID()].getTeam()){
                minimapPaint.setColor(0x00ff00);
            } else if(GameThread.getPlayerArray()[i].getInvisible()){
                break;
            } else{
                minimapPaint.setColor(0xff0000);
            }
            minimapPaint.setAlpha(MINIMAP_ALPHA);
            canvas.drawCircle((((float)(MINIMAP_ORIGINAL_BITMAP.getWidth()-2*BORDER_TILES_RIGHT-2)/(float)(MINIMAP_ORIGINAL_BITMAP.getWidth()-2*BORDER_TILES_RIGHT))*(GameThread.getPlayerArray()[i].getXCoordinate()-BORDER_TILES_RIGHT)+1)*MINIMAP_SCALE +GameSurface.getSurfaceWidth()-MINIMAP_BITMAP.getWidth(),
                    (((float)(MINIMAP_ORIGINAL_BITMAP.getHeight()-2*BORDER_TILES_TOP-2)/(float)(MINIMAP_ORIGINAL_BITMAP.getHeight()-2*BORDER_TILES_TOP))*(GameThread.getPlayerArray()[i].getYCoordinate()-BORDER_TILES_TOP)+1)*MINIMAP_SCALE, MINIMAP_SCALE, minimapPaint);
        }

    }

    public boolean getSolid(int xTileCoordinate, int yTileCoordinate){
        return TILE_MAP[xTileCoordinate][yTileCoordinate].SOLID;
    }

    public static float getStartX(byte team) {
        if (team == 0){
            blueCoordinateDistributionIndex++;
            return playerStartCoordinates[blueCoordinateDistributionIndex][0];
        }else {
            greenCoordinateDistributionIndex++;
            return playerStartCoordinates[greenCoordinateDistributionIndex + 4][0];
        }
    }

    public static float getStartY(byte team) {
        if (team == 0){
            return playerStartCoordinates[blueCoordinateDistributionIndex][1];
        }else {
            return playerStartCoordinates[greenCoordinateDistributionIndex + 4][1];
        }
    }

    public class MiniMapSurface extends SurfaceView {
        public MiniMapSurface(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }

    public static LightBulbStand[] getFriendlyLightBulbStands(byte team){
        if (team == 0){
            return blueLightBulbStandArray;
        }else {
            return greenLightBulbStandArray;
        }
    }

    public static LightBulbStand[] getHostileLightBulbStands(byte team){
        if (team == 1){
            return blueLightBulbStandArray;
        }else {
            return greenLightBulbStandArray;
        }
    }
}
