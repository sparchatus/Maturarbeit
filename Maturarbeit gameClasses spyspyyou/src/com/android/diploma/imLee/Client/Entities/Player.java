package com.android.diploma.imLee.Client.Entities;

public class Player extends Entity {

	public final int MAX_HEALTH;
	public final int TEAM_NUMBER;
	private final int PLAYER_ID;

	public boolean isDead;
	/**
	 * Number of seconds after the death of the player until he respawns.
	 */
	private int deathTimer;
	public int currentHealth;

	/**
	 * 
	 * @param xCoordinate
	 *            The starting x-Coordinate of the player.
	 * @param yCoordinate
	 *            The starting y-Coordinate of the player.
	 */
	public Player(float xCoordinate, float yCoordinate, int playerID, int teamNumber, Equipment.EquipmentTypes playerEquipment) {
		super(xCoordinate, yCoordinate);
		isDead = false;
		PLAYER_ID = playerID;
		TEAM_NUMBER = teamNumber;
		MAX_HEALTH = Equipment.equipPlayer(this, playerEquipment);
	}

	public void applyDamage(int damageToPlayer) {
		currentHealth -= damageToPlayer;
		if (currentHealth <= 0) {
			playerDeath();
		}
	}

	public void playerDeath() {
		currentHealth = MAX_HEALTH;
		deathTimer += 5;
	}

	public void update() {
		heal(1);
	}

	public void heal(int healingAmount) {
		currentHealth += healingAmount;
	}
}
