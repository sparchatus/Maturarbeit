package ch.imlee.maturarbeit.events.gameStateEvents;


import ch.imlee.maturarbeit.activities.GameClient;

// this game is sent by the host when he ends an ongoing game. This Event is not implemented yet
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

    public static void giveActivity(GameClient gameClientActivity){
      gameClient = gameClientActivity;
    }
}
