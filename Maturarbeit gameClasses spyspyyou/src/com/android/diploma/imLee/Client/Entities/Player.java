package com.android.diploma.imLee.Client.Entities;

import com.android.diploma.imLee.Client.GameLoop;

public class Player extends Entity {

	public final int MAX_HEALTH;
	public final boolean IS_ENEMY;
	protected final int PLAYER_ID;
	public final int TEAM_NUMBER;

	public boolean isDead;
	public boolean hasFlag;

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
		hasFlag = false;
		TEAM_NUMBER = teamNumber;
		PLAYER_ID = playerID;
		IS_ENEMY = (teamNumber != GameLoop.playerList.get(0).TEAM_NUMBER);
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
		xCoordinate = 0;
		yCoordinate = 0;
	}

	public void heal(int healingAmount) {
		currentHealth += healingAmount;
	}
}
