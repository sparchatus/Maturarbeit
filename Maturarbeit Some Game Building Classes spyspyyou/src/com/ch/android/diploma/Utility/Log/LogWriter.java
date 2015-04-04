package com.ch.android.diploma.Utility.Log;

import java.io.IOException;
import java.io.PipedInputStream;

import com.ch.android.diploma.GameVersion;
import com.ch.android.diploma.Utility.TxtWriter;

public class LogWriter {

	private PipedInputStream logInStream;
	private TxtWriter logWriter;
	private String buildingLog;
	private char nextChar;

	public LogWriter(PipedInputStream logInStream, String logFileName) {
		this.logInStream = logInStream;
		logWriter = new TxtWriter("Log Files/" + logFileName);
		buildingLog = "";
		logWriter.append(GameVersion.VERSION);
	}

	public void update() {
		try {
			if (logInStream.available() != 0) {
				writeNewLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logWriter.append(buildingLog);
	}

	private void writeNewLine() {
		buildingLog = "";
		try {
			while ((nextChar = (char) logInStream.read()) != '/') {
				buildingLog += nextChar;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
