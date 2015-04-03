package com.ch.android.diploma;

public class GameLoop {
	private final int TIME_PER_TICK;

	private boolean running;

	public int synchronizedTick;
	private long tickSpareTime;

	private long lastTickTime;

	public GameLoop(int gameMiliSecPerTick) {
		TIME_PER_TICK = gameMiliSecPerTick;
	}

	public void loop() {
		while (running) {
			lastTickTime = System.currentTimeMillis();

			update();
			render();

			if ((tickSpareTime = System.currentTimeMillis() - (lastTickTime + TIME_PER_TICK)) > 0) {
				try {
					Thread.sleep(tickSpareTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else{
				//lag occurred! running behind? Check Server Tick!
			}
		}
	}

	public void update() {

	}

	public void render() {

	}

}
