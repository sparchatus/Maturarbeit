package com.ch.android.diploma.Client.Entities;

public class ThisPlayer extends Player {

	public ThisPlayer(float playerXCoordinate, float playerYCoordinate, int playerID, int playerTeamNumber, int playerEquipmentNumber, int playerMaxHealth, int playerBombType, int playerParticleType) {
		super(playerXCoordinate, playerYCoordinate, playerID, playerTeamNumber, playerEquipmentNumber, playerMaxHealth, playerBombType, playerParticleType);
		thisPlayer = true;
	}

}
