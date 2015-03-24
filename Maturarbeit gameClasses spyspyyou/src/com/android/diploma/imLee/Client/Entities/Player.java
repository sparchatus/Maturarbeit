package com.android.diploma.imLee.Client.Entities;

public class Player extends Entity {

	private enum Equipment {

	}

	private int defaultHealth, currentHealth;

	private Equipment equipment;
	/**
	 * 0 for team one and 1 for team two.
	 */
	private int team;

	/**
	 * 
	 * @param xCoordinate
	 *            The starting x-Coordinate of the player.
	 * @param yCoordinate
	 *            The starting y-Coordinate of the player.
	 * @param playerHealth
	 *            The defaut health the player has got.
	 * @param playerEquipment
	 *            The equipment the player chose at gamesetup.
	 * @param playerTeam
	 *            The team the player is on.
	 */
	public Player(float xCoordinate, float yCoordinate, int playerHealth, String playerEquipment, int playerTeam) {
		super(xCoordinate, yCoordinate);
		this.defaultHealth  = currentHealth = playerHealth;
		this.team = playerTeam;
	}

	/**
	 * @return the currentHealth of the player
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * @return the equipment of the player
	 */
	public Equipment getEquipment() {
		return equipment;
	}
}
