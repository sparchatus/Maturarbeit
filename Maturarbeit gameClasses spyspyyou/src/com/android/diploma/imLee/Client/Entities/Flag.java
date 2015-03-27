package com.android.diploma.imLee.Client.Entities;

import com.android.diploma.imLee.Client.GameLoop;

public class Flag extends Entity {

	private final int TEAM_NUMBER;

	private boolean isInBase;

	private Player flagOwner;

	private Player capturingEnemy;

	private int flagResetTick;

	public Flag(float xCoordinate, float yCoordinate, byte teamNumber) {
		super(xCoordinate, yCoordinate);
		TEAM_NUMBER = teamNumber;
		flagOwner = null;
		capturingEnemy = null;
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
			if (capturingEnemy == null) {
				for (Player enemyPlayer : GameLoop.playerList) {
					if()
				}
			}
		}
	}

	public void render() {

	}
}
