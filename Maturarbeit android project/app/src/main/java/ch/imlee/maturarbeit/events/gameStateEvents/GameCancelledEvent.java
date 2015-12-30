package ch.imlee.maturarbeit.events.gameStateEvents;


import ch.imlee.maturarbeit.activities.GameClient;

// this game is sent by the host when he ends an ongoing game.
public class GameCancelledEvent extends GameStateEvent {

    private static GameClient gameClient;

    @Override
    public String toString(){
        return super.toString() + 'C';
    }

    @Override
    public boolean handle(byte i){
        gameClient.onBackPressed();
        return true;
    }

    // a reference to the GameClient Object is required to finish the Activity
    public static void giveActivity(GameClient gameClientActivity){
      gameClient = gameClientActivity;
    }
}
