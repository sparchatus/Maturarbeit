package com.android.diploma.imLee.Client.Entities;

public class BombEntity extends Entity {

	private final int explosionTick;
	private final int maxDamage;
	private final int damageRadius;

	public BombEntity(float xCooriateBomb, float yCooriateBomb, int bombExpolsionTick, int bombMaxDamage, int bombDamageRadius) {
		super(xCooriateBomb, yCooriateBomb);
		this.explosionTick = bombExpolsionTick;
		maxDamage = bombMaxDamage;
		damageRadius = bombDamageRadius;
	}

	/**
	 * 
	 * @param synchronizedTick
	 * @param userPlayer
	 * 
	 * @return True if the bomb detonated and thus needs to be removed from the
	 *         bombList.
	 * @see #bombList
	 */
	public boolean bombTrigger(int synchronizedTick, Player userPlayer) {
		if (explosionTick <= synchronizedTick) {
			userPlayer.applyDamage((int) (maxDamage / damageRadius * Math.sqrt(Math.pow(userPlayer.getxCoordinate() - xCoordinate, 2) + Math.pow(userPlayer.getyCoordinate() - yCoordinate, 2))));
			return true;
		}
		return false;
	}

	/**
	 * @return the explosionTick
	 */
	public int getExplosionTick() {
		return explosionTick;
	}

	public void render() {

	}

}
