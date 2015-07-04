package ch.imlee.maturarbeit.game.entity;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.events.gameActionEvents.SlimeEvent;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 11.06.2015.
 */
public class Slime extends User {

    private final int MANA_CONSUMPTION = MAX_MANA / 100;

    public Slime(float entityXCoordinate, float entityYCoordinate, Map map, GameThread gameThread, byte team, byte playerId) {
        super(entityXCoordinate, entityYCoordinate, PlayerType.SLIME, map, gameThread, team, playerId, null);
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
        if (mana >= MAX_MANA){
            mana = MAX_MANA;
        }
    }

    @Override
    public void skillActivation() {
        if (slimy){
            slimy = false;
        }else {
            if (mana <= 10*MANA_CONSUMPTION)return;
            slimy = true;
        }
        new SlimeEvent(ID, slimy).send();
    }
}
