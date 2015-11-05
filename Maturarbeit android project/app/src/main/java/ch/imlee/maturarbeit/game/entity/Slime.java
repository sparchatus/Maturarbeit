package ch.imlee.maturarbeit.game.entity;

import ch.imlee.maturarbeit.activities.GameClient;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.events.gameActionEvents.SlimeEvent;

/**
 * Created by Sandro on 11.06.2015.
 */
public class Slime extends User {

    private final int MANA_CONSUMPTION = MAX_MANA / 100;

    public Slime(Map map, byte team, byte playerId, String name) {
        super(PlayerType.SLIME, map, team, playerId, name);
        slimy = false;
    }

    @Override
    public void update() {
        super.update();
        if (slimy){
            if (mana <= 0){
                skillActivation();
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
        SlimeEvent slimeEvent;
        if(!slimy && mana > 10 * MANA_CONSUMPTION) {
            slimeEvent = new SlimeEvent(ID, true);
        } else{
            slimeEvent = new SlimeEvent(ID, false);
        }
        slimeEvent.send();
        slimeEvent.apply();
    }
}
