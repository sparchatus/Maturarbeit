package com.android.diploma.imLee.Client.Entities;

import com.android.diploma.imLee.Client.Entities.Equipment.EquipmentTypes;

public class ThisPlayer extends Player {

	private int deathTimer;

	public ThisPlayer(float xCoordinate, float yCoordinate, int playerID, int teamNumber, EquipmentTypes playerEquipment) {
		super(xCoordinate, yCoordinate, playerID, teamNumber, playerEquipment);

		deathTimer = 5;

	}

	public void move(float xDirection, float yDirection) {

	}

	@Override
	public void playerDeath() {
		currentHealth = MAX_HEALTH;
		xCoordinate = 0;
		yCoordinate = 0;
		deathTimer += 5;
	}
}
