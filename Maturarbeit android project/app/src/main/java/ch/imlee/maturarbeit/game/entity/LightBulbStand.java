package ch.imlee.maturarbeit.game.entity;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.Map;

/**
 * Created by Sandro on 10.08.2015.
 */
public class LightBulbStand extends Player {
    public LightBulbStand(float entityXCoordinate, float entityYCoordinate, PlayerType type, Map map, GameThread gameThread, byte Team, byte playerId, User theUser) {
        super(entityXCoordinate, entityYCoordinate, type, map, gameThread, Team, playerId, theUser);
    }
}
