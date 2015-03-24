package com.android.diploma.imLee.Client.Entities;

public class Player extends Entity {

	private enum Equipment {
		
	}

	private int health;

	private Equipment equipment;

	public Player(float xCoordinate, float yCoordinate, int playerHealth, Equipment playerEquipment) {
		super(xCoordinate, yCoordinate);
		this.health = playerHealth;
		this.equipment = playerEquipment;
	}

	/**
	 * @return the health of the player
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @return the equipment of the player
	 */
	public Equipment getEquipment() {
		return equipment;
	}
}
