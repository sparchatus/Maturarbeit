package ch.imlee.maturarbeit.game.entity;

import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.events.gameActionEvents.SlimeEvent;

public class Slime extends User {

    // this consumption is measured in mana per TICK
    private final int MANA_CONSUMPTION = MAX_MANA / 100;
    private final int MANA_REGENERATION = MANA_CONSUMPTION / 5;

    public Slime(Map map, byte team, byte playerId, String name) {
        super(PlayerType.SLIME, map, team, playerId, name);
        slimy = false;
    }

    @Override
    public void update() {
        super.update();
        // Slime degenerates mana when his skill is active
        if (slimy){
            mana -= MANA_CONSUMPTION;
            // when out of mana, the skill is deactivated
            if (mana < 0){
                skillActivation();
            }
        }
        // mana generation happenes automatically
        mana += MANA_REGENERATION;
        // the mana is capped at MAX_MANA
        if (mana >= MAX_MANA){
            mana = MAX_MANA;
        }
    }

    // Slime has a toggle skill
    @Override
    public void skillActivation() {
        SlimeEvent slimeEvent;
        // the Slime can only toggle his skill to true if he has a certain minimum of mana
        if(!slimy && mana > Tick.TICK * 2 / 5 * MANA_CONSUMPTION) {
            slimeEvent = new SlimeEvent(ID, true);
        } else{
            slimeEvent = new SlimeEvent(ID, false);
        }
        slimeEvent.send();
        slimeEvent.apply();
    }

    @Override
    protected void death(String deathReason) {
        super.death(deathReason);
        if (slimy){
            // upon dying the skill is deactivated if it was activated before
            skillActivation();
        }
    }
}
