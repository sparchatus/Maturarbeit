package com.ch.android.diploma;

import com.ch.android.diploma.Client.Event.PlayerData;

public class StartingKlasseNurF�rEclipseTests {
	private static CaptureTheFlag game;

	public static Thread gameThread;

	static// parameters needed to start the game
	int numberOfPlayers, thisPlayerID;
	static PlayerData[] playerData;

	public static void main(String[] args) {
		game = new CaptureTheFlag(numberOfPlayers, thisPlayerID, playerData);
		gameThread = new Thread(game);
	}
}
