package ch.imlee.maturarbeit.game.events;

import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 15.06.2015.
 */
public class PlayerMotionEvent extends Event{

    @Override
    public String toEventString() {
        return "P";
    }

    @Override
    public void apply(GameSurface.GameThread gameThread) {

    }
}
