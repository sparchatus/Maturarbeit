package ch.imlee.maturarbeit.game.entity;

import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.events.gameActionEvents.SlimeEvent;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 11.06.2015.
 */
public class Slime extends User {

    private final int MANA_CONSUMPTION = MAX_MANA / 100;

    public Slime(float entityXCoordinate, float entityYCoordinate, PlayerType type, Map map, GameSurface.GameThread gameThread, byte team, byte playerId, User theUser) {
        super(entityXCoordinate, entityYCoordinate, type, map, gameThread, team, playerId, theUser);
        slimy = false;
    }

    @Override
    public void update() {
        super.update();
        if (slimy){
            if (mana <= 0){
                slimy = false;
            }else {
                mana -= MANA_CONSUMPTION;
            }
        }
        mana += 2;
    }

    @Override
    public void skillActivation() {
        if (slimy){
            slimy = false;
        }else {
            slimy = true;
        }
        new SlimeEvent(ID, slimy).send();
    }
}
