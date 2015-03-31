package com.ch.android.diploma.Utility.Log;

import java.io.IOException;
import java.io.PipedOutputStream;

public class LogCollector {

	public static PipedOutputStream logOutStream = new PipedOutputStream();

	private static enum LogStates {
		STACKTRACE, ERROR, DEBUG, INFO
	}

	public static LogStates logState;

	public static void infoLog(String infoLog) {
		if (logState.ordinal() == 3) {
			try {
				logOutStream.write(infoLog.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void debugLog(String debugLog) {
		if (logState.ordinal() >= 2) {
			try {
				logOutStream.write(debugLog.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void ErrorLog(String errorLog) {
		if (logState.ordinal() >= 1) {
			try {
				logOutStream.write(errorLog.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void StackTraceLog(String stackTraceLog) {
		try {
			logOutStream.write(stackTraceLog.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setLogState(String newLogState) {
		logState = LogStates.valueOf(newLogState);
	}
}
