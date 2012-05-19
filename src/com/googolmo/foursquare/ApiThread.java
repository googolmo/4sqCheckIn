/**
 * 
 */
package com.googolmo.foursquare;

import android.os.Handler;

/**
 * @author googolmo
 * 
 */
public class ApiThread extends Thread {

	private Handler mHandler;

	public ApiThread(Handler handler) {
		this.mHandler = handler;
	}

	@Override
	public void run() {

		super.run();
	}

}
