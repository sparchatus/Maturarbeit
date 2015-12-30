package ch.imlee.maturarbeit.game.entity;

import ch.imlee.maturarbeit.game.GameThread;
import ch.imlee.maturarbeit.game.map.Map;
import ch.imlee.maturarbeit.events.gameActionEvents.StunEvent;
import ch.imlee.maturarbeit.views.GameSurface;

public class Fluffy extends User {

    // the unit is mana per activation
    private int MANA_CONSUMPTION = MAX_MANA;

    public Fluffy(Map map, byte team, byte playerId, String name) {
        super(PlayerType.FLUFFY, map, team, playerId, name);
    }

    @Override
    public void update() {
        super.update();
        // this player type generates mana by moving around. the faster he moves the more mana he gets
        mana += 8 * speed / MAX_SPEED;
        // the mana is capped at MAX_MANA
        if (mana >= MAX_MANA){
            mana = MAX_MANA;
        }
    }

    public void skillActivation() {
        // getting the focused player from the controller of the GameSurface
        Player focusedPlayer = GameSurface.getFocusedPlayer();
        //only with sufficient mana and a player as focus this player type is able to activate his skill
        if (mana == MAX_MANA && focusedPlayer != null) {
            focusedPlayer.stun(GameThread.getSynchronizedTick() + STUN_TIME);
            // sending the information to the other devices
            new StunEvent(ID, focusedPlayer.getID(), GameThread.getSynchronizedTick() + STUN_TIME).send();
            // reset the focus
            GameSurface.nullFocusedPlayer();
            // consuming the mana
            mana -= MANA_CONSUMPTION;
        }
    }
}
