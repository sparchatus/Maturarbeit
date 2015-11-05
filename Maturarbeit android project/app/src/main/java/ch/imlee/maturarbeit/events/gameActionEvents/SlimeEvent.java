package ch.imlee.maturarbeit.events.gameActionEvents;

import ch.imlee.maturarbeit.game.GameThread;

/**
 * Created by Sandro on 18.06.2015.
 */
public class SlimeEvent extends GameActionEvent{

    private boolean slimy;

    public SlimeEvent(String eventString){
        super(Byte.valueOf(eventString.substring(eventString.length() - 1)));
        senderID = Byte.valueOf(eventString.substring(2, 3));
        if (eventString.charAt(3) == '0'){
            slimy = false;
        }else {
            slimy = true;
        }
    }

    public SlimeEvent(byte playerId, boolean slimy){
        super(playerId);
        this.slimy = slimy;
    }

    @Override
    public String toString() {
        if (slimy) {
            return super.toString() + "L" + senderID + '1'+ "i" + senderID;
        }else {
            return super.toString() + "L" + senderID + '0'+ "i" + senderID;
        }
    }

    @Override
    public void apply() {
        GameThread.getPlayerArray()[senderID].setSlimy(slimy);
        /*
        if(slimy){
            GameThread.getPlayerArray()[senderID].getSlimeSound().start();
        } else{
            GameThread.getPlayerArray()[senderID].getSlimeSound().stop();
        }
        */
    }
}
