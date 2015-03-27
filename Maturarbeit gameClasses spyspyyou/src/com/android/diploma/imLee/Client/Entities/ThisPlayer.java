package com.android.diploma.imLee.Client.Entities;

import com.android.diploma.imLee.Client.Comuncation.SendToServer;
import com.android.diploma.imLee.Client.Entities.Equipment.EquipmentTypes;

public class ThisPlayer extends Player {

	/**
	 * Number of seconds after the death of the player until he respawns.
	 */
	private int deathTimer;

	public ThisPlayer(float xCoordinate, float yCoordinate, int playerID, int teamNumber, EquipmentTypes playerEquipment) {
		super(xCoordinate, yCoordinate, playerID, teamNumber, playerEquipment);

		deathTimer = 5;

	}

	public void move(float xMovement, float yMovement) {
		if (xMovement == 0 && yMovement == 0) {
			return;
		}
		xCoordinate += xMovement;
		yCoordinate += yMovement;
		SendToServer.playerPosition(xCoordinate, yCoordinate);
	}

	@Override
	public void playerDeath() {
		currentHealth = MAX_HEALTH;
		xCoordinate = 0;
		yCoordinate = 0;
		deathTimer += 5;
	}
}
