package com.ch.android.diploma.Client.Entities;

import com.ch.android.diploma.GameLoop;

public class Player extends Entity {

	// final variables:
	protected final int TEAM_NUMBER;
	protected final int ID;
	protected final int MAX_HEALTH;
	protected final int MAX_BOMB_AMOUNT;
	protected final int PARTICLE_TYPE;
	protected final int BOMB_TYPE;
	protected boolean thisPlayer;

	protected final float SPAWN_X_COORDINATE, SPAWN_Y_COORDINATE;

	// non-final variables:
	protected int health;
	protected int currentBombAmount;
	protected int deathTicks;
	protected int reviveTick;

	public Player(float playerXCoordinate, float playerYCoordinate, int playerID, int playerTeamNumber, int playerEquipmentNumber, int playerMaxHealth, int playerBombType, int playerParticleType) {
		super(playerXCoordinate, playerYCoordinate);
		SPAWN_X_COORDINATE = playerXCoordinate;
		SPAWN_Y_COORDINATE = playerYCoordinate;
		TEAM_NUMBER = playerTeamNumber;
		MAX_HEALTH = playerMaxHealth;
		ID = playerID;
		MAX_BOMB_AMOUNT = 3;
		PARTICLE_TYPE = playerParticleType;
		BOMB_TYPE = playerBombType;
		deathTicks = 100;
		reviveTick = 0;
		thisPlayer = false;
	}

	public void update() {
		if (reviveTick <= GameLoop.synchronizedTick) {
			// do updating here
		}
	}

	public void render() {
		if (reviveTick > GameLoop.synchronizedTick) {
			// draw things grey, add a death timer
		}
	}

	public void applyDamage(int playerDamage) {
		health -= playerDamage;
		if (health <= 0) {
			playerDeath();
		}
	}

	private void playerDeath() {
		xCoordinate = SPAWN_X_COORDINATE;
		yCoordinate = SPAWN_Y_COORDINATE;
		health = MAX_HEALTH;
		currentBombAmount = MAX_BOMB_AMOUNT;
		reviveTick = GameLoop.synchronizedTick + deathTicks;
		deathTicks += 20;
	}
}
