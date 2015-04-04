package com.ch.android.diploma.Client.Entities;

public class Particle extends Entity {

	private float X_VELOCITY, Y_VELOCITY;
	private final int DAMAGE;

	// private final Image...

	public Particle(float particleXCoordinate, float particleYCoordinate, byte particleTypeNumber) {
		super(particleXCoordinate, particleYCoordinate);
		DAMAGE = EquipParticle.equipParticle(this, particleTypeNumber);
	}

	public void update() {
		xCoordinate += X_VELOCITY;
		yCoordinate += Y_VELOCITY;
	}

	public void render() {

	}
}
