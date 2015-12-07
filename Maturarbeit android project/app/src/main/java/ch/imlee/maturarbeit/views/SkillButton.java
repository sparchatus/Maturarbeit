package ch.imlee.maturarbeit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.entity.User;
import ch.imlee.maturarbeit.game.special_screens.EndGameScreen;

/**
 * Created by Sandro on 08.06.2015.
 */
public class SkillButton extends Button{

    public SkillButton(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if(GameThread.getUser().getDead()||GameThread.getUser().getFalling()){
                return super.onTouchEvent(event);
            }
            GameThread.getUser().skillActivation();
        }
        return super.onTouchEvent(event);
    }

}
