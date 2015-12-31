package ch.imlee.maturarbeit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.special_screens.EndGameScreen;

public class SkillButton extends Button{

    public SkillButton(Context context, AttributeSet attrs) {
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
        // only touching down is of interest because the SkillButton does nod have to be held down
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            // in these cases the User isn't able to act
            if(GameThread.getUser().getDead()||GameThread.getUser().getFalling()){
                return super.onTouchEvent(event);
            }
            GameThread.getUser().skillActivation();
        }
        return super.onTouchEvent(event);
    }
}
