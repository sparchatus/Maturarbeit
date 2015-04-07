package com.ch.android.diploma;

import java.util.ArrayList;
import java.util.List;

import com.ch.android.diploma.Client.Entities.Player;
import com.ch.android.diploma.Client.Entities.ThisPlayer;
import com.ch.android.diploma.Client.Entities.Bombs.Bomb;
import com.ch.android.diploma.Client.Entities.Bombs.TimeBomb;
import com.ch.android.diploma.Client.Event.BombEventInterface;
import com.ch.android.diploma.Client.Event.Event;
import com.ch.android.diploma.Client.Event.PlayerStartData;

public class CaptureTheFlag extends GameLoop implements BombEventInterface {

	private final Player[] playerArray;

	private List<Bomb> bombList = new ArrayList<Bomb>();

	public CaptureTheFlag(int numberOfPlayers, int thisPlayerID, PlayerStartData[] playerDataPackage) {
		playerArray = new Player[numberOfPlayers];
		for (PlayerStartData playerData : playerDataPackage) {
			if (playerData.ID == thisPlayerID) {
				playerArray[playerData.ID] = new ThisPlayer(playerDataPackage[playerData.ID].xCoordinate, playerDataPackage[playerData.ID].yCoordinate, playerData.ID, playerDataPackage[playerData.ID].TeamNumber, playerDataPackage[playerData.ID].equipmentNumber, playerDataPackage[playerData.ID].maxHealth, playerDataPackage[playerData.ID].bombType, playerDataPackage[playerData.ID].particleType);
				referenceToThisPlayer = (ThisPlayer) playerArray[playerData.ID];
			} else {
				playerArray[playerData.ID] = new Player(playerDataPackage[playerData.ID].xCoordinate, playerDataPackage[playerData.ID].yCoordinate, playerData.ID, playerDataPackage[playerData.ID].TeamNumber, playerDataPackage[playerData.ID].equipmentNumber, playerDataPackage[playerData.ID].maxHealth, playerDataPackage[playerData.ID].bombType, playerDataPackage[playerData.ID].particleType);
			}
		}
	}

	@Override
	protected void update() {
		if (!eventList.isEmpty()) {
				for (Event currentEvent : eventList) {
					if (currentEvent.eventType == Event.EventTypes.ADD_BOMB_EVENT)
						addBomb();
				}
		}
	}

	@Override
	protected void render() {

	}

	@Override
	public void addBomb() {
			
	}
}
