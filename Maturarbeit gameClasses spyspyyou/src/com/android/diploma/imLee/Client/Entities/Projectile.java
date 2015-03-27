package com.android.diploma.imLee.Client.Entities;

public class Projectile extends Entity {

	private final int X_VELOCITY;
	private final int Y_VELOCITY;
	private final int TEAM_NUMBER;

	public Projectile(float xCoordinate, float yCoordinate, int xVelocity, int yVelocity, int projectileTeamNumber) {
		super(xCoordinate, yCoordinate);
		X_VELOCITY = xVelocity;
		Y_VELOCITY = yVelocity;
		TEAM_NUMBER = projectileTeamNumber;
	}

	public void update() {
		xCoordinate += X_VELOCITY;
		yCoordinate += Y_VELOCITY;
	}
}
