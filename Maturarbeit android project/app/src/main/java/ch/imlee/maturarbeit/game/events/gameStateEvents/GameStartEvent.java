package ch.imlee.maturarbeit.game.events.gameStateEvents;

import android.content.Intent;

import ch.imlee.maturarbeit.bluetooth.Util;
import ch.imlee.maturarbeit.game.GameClient;

/**
 * Created by Lukas on 17.06.2015.
 */
public class GameStartEvent extends GameStateEvent {
    @Override
    public String toString(){
        return super.toString() + 'S';
    }

    @Override
    public void handle(){
        // start the game (finally)
        Util.c.startActivity(new Intent(Util.c, GameClient.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
