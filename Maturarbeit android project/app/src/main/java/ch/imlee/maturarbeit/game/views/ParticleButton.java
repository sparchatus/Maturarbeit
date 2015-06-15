package ch.imlee.maturarbeit.game.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import ch.imlee.maturarbeit.game.User;

/**
 * Created by Sandro on 08.06.2015.
 */
public class ParticleButton extends Button {
    private User user;

    public ParticleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            user.setShooting(true);
        }else if (event.getAction() == MotionEvent.ACTION_UP){
            user.setShooting(false);
        }
        return super.onTouchEvent(event);
    }

    public void setUser(User gameUser){
        user = gameUser;
    }
}

