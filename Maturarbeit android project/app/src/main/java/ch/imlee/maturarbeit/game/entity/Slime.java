package ch.imlee.maturarbeit.game.entity;

import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.events.gameActionEvents.SlimeEvent;

public class Slime extends User {

    private final int MANA_CONSUMPTION = MAX_MANA / 100;

    public Slime(Map map, byte team, byte playerId, String name) {
        super(PlayerType.SLIME, map, team, playerId, name);
        slimy = false;
    }

    @Override
    public void update() {
        super.update();
        // Slime degenerates mana when his skill is active
        if (slimy){
            if (mana <= 0){
                skillActivation();
            }else {
                mana -= MANA_CONSUMPTION;
            }
        }
        mana += MANA_CONSUMPTION / 5;
        // the mana is capped at MAX_MANA
        if (mana >= MAX_MANA){
            mana = MAX_MANA;
        }
    }

    // Ghost has a toggle skill
    @Override
    public void skillActivation() {
        SlimeEvent slimeEvent;
        if(!slimy && mana > Tick.TICK * 2 / 5 * MANA_CONSUMPTION) {
            slimeEvent = new SlimeEvent(ID, true);
        } else{
            slimeEvent = new SlimeEvent(ID, false);
        }
        slimeEvent.send();
        slimeEvent.apply();
    }

    @Override
    protected void death() {
        super.death();
        if (slimy){
            skillActivation();
        }
    }
}
