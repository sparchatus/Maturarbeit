package com.android.diploma.imLee.Client.Entities;

public abstract class Entity {

	private float xCoordinate, yCoordinate;

	public Entity(float xCoordinate, float yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}

	/**
	 * @return the xCoordinate
	 */
	public float getxCoordinate() {
		return xCoordinate;
	}

	/**
	 * @param xCoordinate
	 *            the xCoordinate to set
	 */
	public void setxCoordinate(float xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	/**
	 * @return the yCoordinate
	 */
	public float getyCoordinate() {
		return yCoordinate;
	}

	/**
	 * @param yCoordinate
	 *            the yCoordinate to set
	 */
	public void setyCoordinate(float yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
}
