package com.android.diploma.imLee.Client;

import java.util.ArrayList;
import java.util.List;

import com.android.diploma.imLee.Client.Entities.BombEntity;
import com.android.diploma.imLee.Client.Entities.Player;
import com.android.diploma.imLee.Client.Entities.Projectile;
import com.android.diploma.imLee.Server.ServerThread;

public class GameLoop {

	public static List<Projectile> projectileList = new ArrayList<Projectile>();
	private List<BombEntity> bombList = new ArrayList<BombEntity>();
	public static List<Player> playerList = new ArrayList<Player>();

	private boolean isRunning;
	
	

	private ServerThread server;
	private Thread serverThread;

	private int synchronizedTick;

	public GameLoop(boolean isServer) {
		isRunning = true;
		if (isServer) startServer();
		else clientGameLoop();
	}

	private void startServer() {
		serverThread = new Thread(server);
		serverThread.start();
	}

	private void clientGameLoop() {
		while (isRunning == true) {
			update();
			render();
		}
	}

	private void update() {
		for (Projectile projectile : projectileList) {
			projectile.update();
		}
		for (BombEntity bomb : bombList) {
			bomb.bombTrigger(synchronizedTick, playerList.get(0));
		}
	}

	private void render() {
		for (Projectile projectile : projectileList) {
			projectile.update();
		}
	}
}
