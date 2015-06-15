package ch.imlee.maturarbeit.game;

import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 11.06.2015.
 */
public class Ghost extends User{
    public Ghost(float entityXCoordinate, float entityYCoordinate, PlayerType type, Map map, GameSurface.GameThread gameThread, int team, User theUser) {
        super(entityXCoordinate, entityYCoordinate, type, map, gameThread, team, theUser);
    }

    @Override
    public void skillActivation() {

    }
}
