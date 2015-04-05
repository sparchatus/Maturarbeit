package com.ch.android.diploma;

import java.util.ArrayList;
import java.util.List;

import com.ch.android.diploma.Client.Entities.Bomb;
import com.ch.android.diploma.Client.Entities.Player;
import com.ch.android.diploma.Client.Entities.ThisPlayer;
import com.ch.android.diploma.Client.Event.PlayerData;

public class CaptureTheFlag extends GameLoop {

	private final Player[] playerArray;

	private List<Bomb> bombList = new ArrayList<Bomb>();

	public CaptureTheFlag(int numberOfPlayers, int thisPlayerID, PlayerData[] playerData) {
		playerArray = new Player[numberOfPlayers];
		for (PlayerData player : playerData) {
			if (player.ID == thisPlayerID) {
				playerArray[player.ID] = new ThisPlayer(playerData[player.ID].xCoordinate, playerData[player.ID].yCoordinate, player.ID, playerData[player.ID].TeamNumber, playerData[player.ID].equipmentNumber, playerData[player.ID].maxHealth, playerData[player.ID].bombType, playerData[player.ID].particleType);
			} else {
				playerArray[player.ID] = new Player(playerData[player.ID].xCoordinate, playerData[player.ID].yCoordinate, player.ID, playerData[player.ID].TeamNumber, playerData[player.ID].equipmentNumber, playerData[player.ID].maxHealth, playerData[player.ID].bombType, playerData[player.ID].particleType);
			}
		}
	}

	@Override
	protected void update() {
		if (!eventList.isEmpty()) {
			// process the server stated events
		}
	}

	@Override
	protected void render() {

	}
}
