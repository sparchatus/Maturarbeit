package ch.imlee.maturarbeit.game.special_screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.utils.LogView;
import ch.imlee.maturarbeit.views.GameSurface;

public class EndGameScreen {

    // the order of the values is left, top, right, bottom, height / 2 (difference between bottom and top divided by 2)
    protected static final int exitButtonCoords[] = {0, 0, GameSurface.getSurfaceWidth() / 2, GameSurface.getSurfaceHeight(), GameSurface.getSurfaceHeight()};
    protected static final int restartButtonCoords[] = {GameSurface.getSurfaceWidth() / 2, 0, GameSurface.getSurfaceWidth(), GameSurface.getSurfaceHeight(), GameSurface.getSurfaceHeight()};

    // used for the rendering
    private static SurfaceHolder holder;

    // tells the subclass ServerEndGameScreen what button the User pressed
    protected static boolean isExit, isRestart;

    // the time when the EndGameScreen started
    // this is used to avoid the Buttons being pressed accidentally by disabling them in the first few seconds
    private static long startTime;

    // the time during which the Buttons are deactivated
    private static int waitTime = 2000;

    //
    public void endGameLoop(SurfaceHolder surfaceHolder){
        //setup
        holder = surfaceHolder;
        isRestart = isExit = false;
        startTime = System.currentTimeMillis();

        // update and render loop
        while(GameThread.getEndGameActive()){
            update();
            render();
            try {
                // the precise periodicity of this loop is not of importance thus the sleep time isn't calculated but constant
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // drawing the text onto the screen and allow the server via the subRender to draw the buttons
    protected void render(){
        Canvas c = null;
        try {
            c = holder.lockCanvas(null);
            synchronized (holder) {
                if (c != null) {
                    c.drawColor(Color.BLACK);
                    subRender(c);
                    LogView.render(c);
                }
            }
        } finally {
            if (c != null) {
                holder.unlockCanvasAndPost(c);
            }
        }
    }

    protected void update(){
        LogView.update();
    }

    // this render method is overwritten by the ServerEndGameScreen to draw the buttons
    protected void subRender(Canvas canvas){
        // some transparent black ove every thing
        Paint paint = new Paint();
        paint.setTextSize(64);
        paint.setColor(Color.WHITE);
        // finish text in white
        canvas.drawText("Game Finished!", 10, 10 + paint.getTextSize(), paint);
        // winner in corresponding color
        if (GameThread.getWinningTeam() == 0) {
            paint.setColor(Color.BLUE);
            canvas.drawText("Team Blue won!", 10, 20 + 2 * paint.getTextSize(), paint);
        } else if (GameThread.getWinningTeam() == 1) {
            paint.setColor(Color.GREEN);
            canvas.drawText("Team Green won!", 10, 20 + 2 * paint.getTextSize(), paint);
        }
    }

    // the EndGame Screen has to handle the Events because the on Touch method in the SeverEndGameScreen could not be reached as easily
    // the exit button fills the left, the restart button the right half of the screen
    public static boolean onTouch(MotionEvent event){
        // if a button was pressed or the buttons are still deactivated then the MOtionEvent is ignored
        if(isExit || isRestart || System.currentTimeMillis() - startTime < waitTime){
            return false;
        }else if(event.getX() < exitButtonCoords[3]){
            isExit = true;
            Log.e("Endgame", "exit");
        }else {
            isRestart = true;
            Log.e("Endgame", "restart");
        }
        return false;
    }
}
