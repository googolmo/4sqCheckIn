package com.googolmo.foursquare.utils;

import android.util.Log;

public class LogUtil {

	private final String tag;

	public static LogUtil getLog(String name) {
		return new LogUtil(name);
	}

	public LogUtil(String name) {
		this.tag = name.getClass().getName();
	}

	public void debug(String msg) {
		if (Configuration.getisDebug()) {
			Log.d(this.tag, msg);
		}
	}

	public void debug(int msg) {
		debug(msg + "");
	}

	public void error(String msg) {
		Log.e(this.tag, msg);
	}

	public void error(int msg) {
		error(msg + "");
	}

	public void error(String msg, Throwable tr) {
		Log.e(this.tag, msg, tr);
	}

	public void error(int msg, Throwable tr) {
		error(msg + "", tr);
	}

	public void info(String msg) {
		Log.i(this.tag, msg);
	}

	public void inof(int msg) {
		info(msg + "");
	}

	public void warn(String msg) {
		Log.w(this.tag, msg);
	}

	public void warn(int msg) {
		warn(msg + "");
	}

	public void warn(String msg, Throwable tr) {
		Log.w(this.tag, msg, tr);
	}

	public void warn(int msg, Throwable tr) {
		warn(msg + "", tr);
	}

}
