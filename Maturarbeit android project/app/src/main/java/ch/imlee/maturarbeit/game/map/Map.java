package ch.imlee.maturarbeit.game.map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.game.GameClient;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 04.06.2015.
 */
public class Map implements MapDimensions {
    public final int TILE_SIDE;
    private static float MAP_WIDTH, MAP_HEIGHT;
    private static float TEAM_1_START_X, TEAM_1_START_Y, TEAM_2_START_X, TEAM_2_START_Y;


    private final int MINIMAP_WIDTH = GameClient.getScreenWidth()/4;
    private final int MINIMAP_HEIGHT = (int)(((float)MINIMAP_WIDTH/(float)(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2).getWidth()-2*BORDER_TILES_RIGHT))*
            (float)(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2).getHeight()-2*BORDER_TILES_TOP));
    private final Bitmap MINIMAP_ORIGINAL_BITMAP = BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2);
    private final Bitmap MINIMAP_BITMAP = Bitmap.createScaledBitmap(Bitmap.createBitmap(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2),
            BORDER_TILES_RIGHT-1, BORDER_TILES_TOP-1, BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2).getWidth()-2*(BORDER_TILES_RIGHT-1),
            BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2).getHeight()-2*(BORDER_TILES_TOP-1)), MINIMAP_WIDTH, MINIMAP_HEIGHT, false);
    final float MINIMAP_SCALE = MINIMAP_BITMAP.getWidth()/(BitmapFactory.decodeResource(GameSurface.getRec(), R.drawable.test_map_2).getWidth()-2*BORDER_TILES_RIGHT);
    private static Paint minimapPaint = new Paint();
    private static final int MINIMAP_ALPHA = 0xaa;

    private final Tile[][]TILE_MAP;
    public Map(Resources rec, int pixelMapID) {
        minimapPaint.setAlpha(0xCC);
        TILE_SIDE = GameClient.getScreenHeight() / TILES_IN_SCREEN_HEIGHT;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap pixelMap = BitmapFactory.decodeResource(rec, pixelMapID, options);
        MAP_WIDTH = pixelMap.getWidth();
        MAP_HEIGHT = pixelMap.getHeight();
        TEAM_1_START_X = 8.5f;
        TEAM_1_START_Y = 5.5f;
        TEAM_2_START_X = MAP_WIDTH - 8.5f;
        TEAM_2_START_Y = MAP_HEIGHT - 5.5f;
        Tile voidTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.void_tile), TILE_SIDE, TILE_SIDE, false), false);
        Tile groundTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.ground_tile), TILE_SIDE, TILE_SIDE, false), false);
        Tile wallTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.wall_tile), TILE_SIDE, TILE_SIDE, false), true);
        Tile greenBaseTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.green_base_tile), TILE_SIDE, TILE_SIDE, false), true);
        Tile blueBaseTile = new Tile(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(rec, R.drawable.blue_base_tile), TILE_SIDE, TILE_SIDE, false), true);
        TILE_MAP = new Tile[pixelMap.getWidth()][pixelMap.getHeight()];
        for(int i = 0; i < pixelMap.getHeight(); i++){
            for (int j = 0; j < pixelMap.getWidth(); j++){
                if(pixelMap.getPixel(j, i) == 0xffffffff) {
                    TILE_MAP[j][i] = groundTile;
                }else if(pixelMap.getPixel(j, i) == 0xffff0000) {
                    TILE_MAP[j][i] = wallTile;
                }else if (pixelMap.getPixel(j, i) == 0xff00ff00){
                    TILE_MAP[j][i] = greenBaseTile;
                }else if (pixelMap.getPixel(j, i) == 0xff0000ff){
                    TILE_MAP[j][i] = blueBaseTile;
                }else{
                    TILE_MAP[j][i] = voidTile;
                }
            }
        }
    }

    public void render(Canvas canvas, User user){
        for (int i = - BORDER_TILES_TOP; i <= BORDER_TILES_TOP; i++){
            for (int j = - BORDER_TILES_RIGHT; j <= BORDER_TILES_RIGHT; j++){
                canvas.drawBitmap(TILE_MAP[((int) user.getXCoordinate()) + j][((int) user.getYCoordinate()) + i].BMP, GameClient.getHalfScreenWidth() + (((int) user.getXCoordinate()) + j - user.getXCoordinate()) * user.TILE_SIDE, GameClient.getHalfScreenHeight() + (((int) user.getYCoordinate()) + i - user.getYCoordinate())* user.TILE_SIDE, null);
            }
        }
    }
    public void renderMinimap(Canvas canvas){
        minimapPaint.setAlpha(MINIMAP_ALPHA);
        canvas.drawBitmap(MINIMAP_BITMAP, GameClient.getScreenWidth()-MINIMAP_WIDTH, 0, minimapPaint);

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
            canvas.drawCircle((((float)(MINIMAP_ORIGINAL_BITMAP.getWidth()-2*BORDER_TILES_RIGHT-2)/(float)(MINIMAP_ORIGINAL_BITMAP.getWidth()-2*BORDER_TILES_RIGHT))*(GameThread.getPlayerArray()[i].getXCoordinate()-BORDER_TILES_RIGHT)+1)*MINIMAP_SCALE + GameClient.getScreenWidth()-MINIMAP_BITMAP.getWidth(),
                    (((float)(MINIMAP_ORIGINAL_BITMAP.getHeight()-2*BORDER_TILES_TOP-2)/(float)(MINIMAP_ORIGINAL_BITMAP.getHeight()-2*BORDER_TILES_TOP))*(GameThread.getPlayerArray()[i].getYCoordinate()-BORDER_TILES_TOP)+1)*MINIMAP_SCALE, MINIMAP_SCALE, minimapPaint);
        }

    }

    public boolean getSolid(int xTileCoordinate, int yTileCoordinate){
        return TILE_MAP[xTileCoordinate][yTileCoordinate].SOLID;
    }

    public static float getStartX(byte team) {
        if (team == 0){
            return TEAM_1_START_X;
        }else {
            return TEAM_2_START_X;
        }
    }

    public static float getStartY(byte team) {
        if (team == 0){
            return TEAM_1_START_Y;
        }else {
            return TEAM_2_START_Y;
        }
    }
}
