package ch.imlee.maturarbeit.game.events.gameStateEvents;

import ch.imlee.maturarbeit.game.entity.PlayerType;

/**
 * Created by Lukas on 20.06.2015.
 */
public class PlayerTypeChosenEvent extends GameStateEvent {
    private final PlayerType type;

    public PlayerTypeChosenEvent(PlayerType type){
        this.type = type;
    }

    @Override
    public String toString(){
        return super.toString() + 't' + type.ordinal();
    }
}