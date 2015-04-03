package com.ch.android.diploma.Utility.Log;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.Date;

public class LogCollector {

	public static PipedOutputStream logOutStream = new PipedOutputStream();
	private Date date = new Date();

	private static enum LogStates {
		STACKTRACE, ERROR, DEBUG, INFO
	}

	public static LogStates logState;

	public static void infoLog(String infoLog) {
		if (logState.ordinal() == 3) {
			try {
				logOutStream.write(("/[" + "][INFO]" + infoLog).getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void debugLog(String debugLog) {
		if (logState.ordinal() >= 2) {
			try {
				logOutStream.write(("/[" + "][DEBUG]" +debugLog).getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void ErrorLog(String errorLog) {
		if (logState.ordinal() >= 1) {
			try {
				logOutStream.write(("/[" + "][ERROR]" +errorLog).getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void StackTraceLog(String stackTraceLog) {
		try {
			logOutStream.write(("/[" + "][STLOG]" +stackTraceLog).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setLogState(String newLogState) {
		logState = LogStates.valueOf(newLogState);
	}
}
