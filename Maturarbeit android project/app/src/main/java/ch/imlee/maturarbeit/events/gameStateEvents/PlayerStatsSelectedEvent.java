package ch.imlee.maturarbeit.events.gameStateEvents;

import android.os.Looper;
import android.os.Handler;

import ch.imlee.maturarbeit.activities.DeviceType;
import ch.imlee.maturarbeit.activities.StartActivity;
import ch.imlee.maturarbeit.bluetooth.Host;
import ch.imlee.maturarbeit.game.entity.PlayerType;
import ch.imlee.maturarbeit.activities.ChooseActivity;

// this Event gets sent by clients only. It's used to let the host know what PlayerType and team the client selected
public class PlayerStatsSelectedEvent extends GameStateEvent {

    private final PlayerType TYPE;
    private final byte TEAM;

    public PlayerStatsSelectedEvent(PlayerType type, byte team){
        TYPE = type;
        TEAM = team;
        serverEvent = true;
    }
    public PlayerStatsSelectedEvent(String eventString){
        TYPE = PlayerType.values()[Integer.parseInt(eventString.substring(2,eventString.indexOf(',')))];
        TEAM = Byte.parseByte(eventString.substring(eventString.indexOf(',')+1, eventString.indexOf("i")));
    }

    @Override
    public String toString(){
        return super.toString() + 'T' + TYPE.ordinal() + ',' + TEAM + 'i' + senderID;
    }
    @Override
    public boolean handle(byte id) {
        if (StartActivity.deviceType == DeviceType.HOST) {
            ChooseActivity.gameStartEvent.setPlayer(TYPE,
                    TEAM,
                    id, Host.sockets.get(id-1).getRemoteDevice().getName());
            ++ChooseActivity.playersReady;
            // if every client has selected their stats, the game can be started

            // because we can only change the button's text in the UI thread, we have to do it with a Handler
            Handler setText = new Handler(Looper.getMainLooper());
            setText.post(new Runnable() {
                public void run() {
                    if (ChooseActivity.playersReady == Host.sockets.size()) {
                        ChooseActivity.startGameButton.setClickable(true);
                        ChooseActivity.startGameButton.setText("Start Game");
                    } else {
                        ChooseActivity.startGameButton.setText("Clients Ready: " + ChooseActivity.playersReady + '/' + Host.sockets.size());
                    }
                }
            });
        }

        return true;
    }

}