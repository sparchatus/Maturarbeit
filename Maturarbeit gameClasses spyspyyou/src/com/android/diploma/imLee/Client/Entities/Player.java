package com.android.diploma.imLee.Client.Entities;

public class Player extends Entity {
	
	public final int MAX_HEALTH;
	public final int TEAM_NUMBER;
	private final int PLAYER_ID;
	
	private int currentHealth;
	
	/**
	 * 
	 * @param xCoordinate
	 *            The starting x-Coordinate of the player.
	 * @param yCoordinate
	 *            The starting y-Coordinate of the player.
	 */
	public Player(float xCoordinate, float yCoordinate, int playerID, int teamNumber) {
		super(xCoordinate, yCoordinate);
		PLAYER_ID = playerID; 
		TEAM_NUMBER = teamNumber;
		Equipment.equipPlayer(this);
	}
}
