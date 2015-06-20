package ch.imlee.maturarbeit.game.events.gameStateEvents;

/**
 * Created by Lukas on 20.06.2015.
 */
public class TeamChangedEvent extends GameStateEvent{
    private int team;

    public TeamChangedEvent(int team){
        this.team = team;
    }

    @Override
    public String toString(){
        return super.toString() + 'T' + team;
    }
}
