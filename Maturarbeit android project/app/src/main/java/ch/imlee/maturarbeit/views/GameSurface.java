package ch.imlee.maturarbeit.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ch.imlee.maturarbeit.R;
import ch.imlee.maturarbeit.events.gameActionEvents.ErrorEvent;
import ch.imlee.maturarbeit.events.gameStateEvents.RestartGameEvent;
import ch.imlee.maturarbeit.game.ControllerState;
import ch.imlee.maturarbeit.activities.GameClient;
import ch.imlee.maturarbeit.game.GameServerThread;
import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;
import ch.imlee.maturarbeit.game.StartDataInitializer;
import ch.imlee.maturarbeit.game.WaitUntilLoadedThread;
import ch.imlee.maturarbeit.game.entity.Player;
import ch.imlee.maturarbeit.game.entity.PlayerType;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.special_screens.EndGameScreen;
import ch.imlee.maturarbeit.game.special_screens.LoadingScreen;
import ch.imlee.maturarbeit.utils.LogView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback{

    private static int width, height;

    private static SurfaceHolder holder;
    // the surface controls the lifecycle of the main thread
    private static GameThread gameThread;
    private static GameSurfaceController gameSurfaceController;
    private static Resources rec;

    public GameSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupThread();
        rec = getResources();
        // needs to be called to get the real width and height
        invalidate();
        width = getWidth();
        height = getHeight();
        rec = getResources();
        GameClient.gameSurfaceLoaded();
    }

    // the surface does not change in size during the game
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // ends the main game thread
        destroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (GameThread.getEndGameActive()){
            return EndGameScreen.onTouch(event);
        }
        // if the GameThread wasn't done loading yet it would cause an error because there would be no User
        if (GameThread.getLoading()){
            return false;
        }
        return gameSurfaceController.onTouch(event);
    }

    public static void update(){
        gameSurfaceController.update();
    }

    public static void render(Canvas canvas){
        gameSurfaceController.render(canvas);
    }

    public static void setup(){
        if (GameThread.getUser().getType() == PlayerType.FLUFFY){
            gameSurfaceController = new FluffyGameSurfaceController();
        }else{
            gameSurfaceController = new GameSurfaceController();
        }
    }

    public static void restart(){
        Log.e("GameSurface", "restart soon");
        try{
            Thread.sleep(1000);
        }catch(Exception e){
            e.printStackTrace();
        }
        gameThread.stopEndGame();
        LoadingScreen.setRestart();
        setupThread();
        GameThread.reset();
        if(StartActivity.deviceType == DeviceType.HOST) {
            new WaitUntilLoadedThread().start();
        }
        GameClient.initializeStartData();
        if(StartActivity.deviceType == DeviceType.HOST){
            new RestartGameEvent().send();
        }
    }

    public static void destroy(){
        if(gameThread == null){
            return;
        }
        gameThread.setRunning(false);
        boolean retry = true;
        while(retry){
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setupThread(){
        if(StartActivity.deviceType == DeviceType.HOST){
            gameThread = new GameServerThread(holder);
        }else {
            gameThread = new GameThread(holder);
        }
        gameThread.setRunning(true);
        gameThread.start();
    }

    public static void nullFocusedPlayer(){
        gameSurfaceController.nullFocusedPlayer();
    }

    public static Player getFocusedPlayer(){
        return gameSurfaceController.getFocusedPlayer();
    }

    public GameThread getGameThread(){
        return gameThread;
    }

    public static Resources getRec(){
        return rec;
    }

    public static int getSurfaceWidth(){
        return width;
    }
    public static int getSurfaceHeight(){
        return height;
    }

    private static class GameSurfaceController {
        private boolean posChanged;

        protected double halfSurfaceWidth, halfSurfaceHeight;
        // finger distance relative to the middle of the screen
        private double xFingerDistance, yFingerDistance;

        protected ControllerState controllerState = ControllerState.NULL;

        public GameSurfaceController() {
            halfSurfaceWidth = width / 2;
            halfSurfaceHeight = height / 2;
        }

        public void update() {
            synchronized (gameSurfaceController) {
                // synchronized with the main game loop
                if (controllerState == ControllerState.AIMING && posChanged) {
                    // some trigonometry to calculate the new user angle
                    double angle = Math.acos(xFingerDistance / Math.sqrt(Math.pow(xFingerDistance, 2) + Math.pow(yFingerDistance, 2)));
                    if (yFingerDistance <= 0) {
                        angle *= -1;
                    }
                    GameThread.getUser().setAngle(angle);
                    posChanged = false;
                }
            }
        }

        // required for the subclass called FluffyGameSurfaceController so we can easily call it's render method in the game thread
        public void render(Canvas canvas){

        }

        // this method decides if the event is
        public boolean onTouch(MotionEvent event) {
            synchronized (gameSurfaceController) {
                // when the player lifts the finger the controllerState resets
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    controllerState = ControllerState.NULL;
                    return false;
                }
                // when touching down on the game surface AIMING is activated
                else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    controllerState = ControllerState.AIMING;
                }
                // the position relative to the middle of the screen
                xFingerDistance = event.getX() - halfSurfaceWidth;
                yFingerDistance = event.getY() - halfSurfaceHeight;
                // telling the user that his angle was changed.
                posChanged = true;
                return true;
            }
        }

        public void nullFocusedPlayer(){
            return;
        }

        public Player getFocusedPlayer(){
            return null;
        }
    }

    private static class FluffyGameSurfaceController extends GameSurfaceController{
        private final float MAX_FOCUS_RANGE = 4.0f;
        private float lastPlayerRadius;

        private final Bitmap FOCUS_BMP;
        private Bitmap scaledFocusBmp;
        private Player focusedPlayer;

        public FluffyGameSurfaceController() {
            FOCUS_BMP = BitmapFactory.decodeResource(rec, R.drawable.focus_overlay);
            scaledFocusBmp = Bitmap.createScaledBitmap(FOCUS_BMP, Map.TILE_SIDE, Map.TILE_SIDE, false);
        }

        // this method is only used when the user is playing as Fluffy and it checks if the player who is focused is still in range.
        // also it has to match the size of the overlay to the player size.
        @Override
        public void update() {
            super.update();
            if (focusedPlayer!= null){
                if(Math.pow(focusedPlayer.getXCoordinate() - GameThread.getUser().getXCoordinate(), 2) + Math.pow(focusedPlayer.getYCoordinate() - GameThread.getUser().getYCoordinate(), 2) > MAX_FOCUS_RANGE) {
                    focusedPlayer = null;
                }
                else if (lastPlayerRadius != focusedPlayer.getPlayerRadius()) {
                    lastPlayerRadius = focusedPlayer.getPlayerRadius();
                    scaledFocusBmp = Bitmap.createScaledBitmap(FOCUS_BMP, (int) (lastPlayerRadius * Map.TILE_SIDE * 2), (int) (lastPlayerRadius * Map.TILE_SIDE * 2), false);
                }
            }
        }

        // only the stun overlay has to be rendered here
        @Override
        public void render(Canvas canvas) {
            if (focusedPlayer != null){
                canvas.drawBitmap(scaledFocusBmp, (float) ((focusedPlayer.getXCoordinate() - GameThread.getUser().getXCoordinate() - lastPlayerRadius) * Map.TILE_SIDE + halfSurfaceWidth), (float) ((focusedPlayer.getYCoordinate() - GameThread.getUser().getYCoordinate() - lastPlayerRadius) * Map.TILE_SIDE + halfSurfaceHeight), null);
            }
        }

        // in this system called method the it's checked whether the user has clicked an enemy player.
        @Override
        public boolean onTouch(MotionEvent event) {
            // it needs to be synchronized because two different threads are accessing the focusedPlayer variable, the playerArray and the playerCoordinates
            synchronized (GameThread.getHolder()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // it checks for every player if he is an enemy and in range.
                    for (Player player : GameThread.getPlayerArray()) {
                        if (player.TEAM != GameThread.getUser().TEAM && Math.pow(GameThread.getUser().getXCoordinate() + (event.getX() - halfSurfaceWidth) / Map.TILE_SIDE - player.getXCoordinate(), 2) + Math.pow(GameThread.getUser().getYCoordinate() + (event.getY() - halfSurfaceHeight) / Map.TILE_SIDE - player.getYCoordinate(), 2) <= Math.pow(player.getPlayerRadius(), 2)) {
                            controllerState = ControllerState.FOCUS;
                            focusedPlayer = player;
                            // we don't have to follow this event further
                            return false;
                        }
                    }
                }
            }
            return super.onTouch(event);
        }

        public Player getFocusedPlayer(){
            return focusedPlayer;
        }
        // to reset the focus when the skill is activated
        public void nullFocusedPlayer(){
            focusedPlayer = null;
        }
    }
}