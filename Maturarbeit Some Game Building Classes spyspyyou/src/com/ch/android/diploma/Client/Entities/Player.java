package com.ch.android.diploma.Client.Entities;

public class Player extends Entity {

	protected final byte TEAM_NUMBER;

	protected int health;

	public Player(float playerXCoordinate, float playerYCoordinate, byte playerTeamNumber) {
		super(playerXCoordinate, playerYCoordinate);
		TEAM_NUMBER = playerTeamNumber;
	}

}
