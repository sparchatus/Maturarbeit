package com.ch.android.diploma.Client.Event;

public class PlayerData extends Event {
	public float xCoordinate, yCoordinate;
	public int ID;
	public int TeamNumber;
	public int equipmentNumber;
	public int maxHealth;
	public int bombType;
	public int particleType;

	public PlayerData(float playerXCoordinate, int playerYCoordinate, int playerID, int playerTeamNumber, int playerEquipmentNumber, int playerMaxHealth, int playerBombType, int playerParticleType) {

		xCoordinate = playerXCoordinate;
		yCoordinate = playerYCoordinate;
		ID = playerID;
		TeamNumber = playerTeamNumber;
		equipmentNumber = playerEquipmentNumber;
		maxHealth = playerMaxHealth;
		bombType = playerBombType;
		particleType = playerParticleType;
	}
}
