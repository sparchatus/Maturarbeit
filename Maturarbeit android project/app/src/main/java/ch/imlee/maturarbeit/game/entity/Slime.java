package ch.imlee.maturarbeit.game.entity;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.Tick;
import ch.imlee.maturarbeit.game.events.gameActionEvents.SlimeTrailEvent;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.game.events.gameActionEvents.SlimeEvent;
import ch.imlee.maturarbeit.game.views.GameSurface;

/**
 * Created by Sandro on 11.06.2015.
 */
public class Slime extends User {

    private final int MANA_CONSUMPTION = MAX_MANA / 100;
    private final int SLIME_EJECTION_RATE = Tick.TICK / 5;
    private double lastSlimeEjection = 0;

    public Slime(Map map, byte team, byte playerId) {
        super(PlayerType.SLIME, map, team, playerId);
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
            if(GameThread.getSynchronizedTick()-SLIME_EJECTION_RATE >= lastSlimeEjection){
                lastSlimeEjection = GameThread.getSynchronizedTick();
                SlimeTrail slimeTrail = new SlimeTrail(getXCoordinate(), getYCoordinate());
                GameThread.addSlimeTrail(slimeTrail);
                new SlimeTrailEvent(slimeTrail).send();
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
    }
}
