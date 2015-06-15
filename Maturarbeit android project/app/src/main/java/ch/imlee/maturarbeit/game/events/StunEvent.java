package ch.imlee.maturarbeit.game.events;

import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 15.06.2015.
 */
public class StunEvent extends Event{

    public int playerID;

    public double stunTick;

    public StunEvent(){

    }

    @Override
    public String toEventString() {
        return "S" + "i" + playerID + "t" + stunTick;
    }

    @Override
    public void apply(GameSurface.GameThread gameThread) {

    }
}
