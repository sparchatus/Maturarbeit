package com.android.diploma.imLee.Client.Entities;

public class Flag extends Entity {

	private boolean isInBase;

	private Player flagOwner;

	private int flagResetTick;

	public Flag(float xCoordinate, float yCoordinate) {
		super(xCoordinate, yCoordinate);
		flagOwner = null;
	}

	public void update(int synchronizedTick) {
		if (flagOwner != null) {
			if (flagOwner.isDead) {
				flagOwner.hasFlag = false;
				flagOwner = null;
				flagResetTick = synchronizedTick;
			}
		} else if (!isInBase) {
			if (flagResetTick >= synchronizedTick) {
				isInBase = true;
				xCoordinate = 0;
				yCoordinate = 0;
			}
		} else {
			
		}
	}

	public void render() {

	}
}
