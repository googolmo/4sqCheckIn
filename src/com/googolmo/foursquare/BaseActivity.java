/**
 * 
 */
package com.googolmo.foursquare;

import android.app.Activity;

/**
 * @author googolmo
 * 
 */
public class BaseActivity extends Activity {

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}

	@Override
	public void finish() {

		super.finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

}
