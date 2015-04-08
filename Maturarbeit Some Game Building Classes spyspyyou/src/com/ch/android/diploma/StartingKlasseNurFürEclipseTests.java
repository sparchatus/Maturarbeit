package com.ch.android.diploma;

import com.ch.android.diploma.Client.Loading.PlayerStartData;

public class StartingKlasseNurFürEclipseTests {
	
	private static GameLoop game;

	public static Thread gameThread;

	static// parameters needed to start the game
	int numberOfPlayers, thisPlayerID;
	static PlayerStartData[] playerData;

	public static void main(String[] args) {
		game = new CaptureTheFlag(true, numberOfPlayers, thisPlayerID, playerData);
		gameThread = new Thread(game);
		gameThread.start();
	}
}
