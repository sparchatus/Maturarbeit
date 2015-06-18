package ch.imlee.maturarbeit.game.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import ch.imlee.maturarbeit.game.entity.User;

/**
 * Created by Sandro on 08.06.2015.
 */
public class SkillButton extends Button{
    private User user;

    public SkillButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            user.skillActivation();
        }
        return super.onTouchEvent(event);
    }

    public void setUser(User user){
        this.user = user;
    }
}
