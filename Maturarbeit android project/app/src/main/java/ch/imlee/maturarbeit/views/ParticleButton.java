package ch.imlee.maturarbeit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.special_screens.EndGameScreen;

public class ParticleButton extends Button {

    public ParticleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // if the endGame is active the touchEvent is redirected
        if (GameThread.getEndGameActive()){
            EndGameScreen.onTouch(event);
            return super.onTouchEvent(event);
        }
        // if the GameThread hasn't done loading yet it would cause an error because there would be no User
        if (GameThread.getLoading()){
            return false;
        }
        // the Particle button is active as long as the finger is down
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            GameThread.getUser().setShooting(true);
        }else if (event.getAction() == MotionEvent.ACTION_UP){
            GameThread.getUser().setShooting(false);
        }
        return super.onTouchEvent(event);
    }

}

