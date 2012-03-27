/**
 * 
 */
package com.googolmo.foursquare;

import com.googolmo.foursquare.app.OAuthAcitivity;

import android.app.Activity;
import android.content.Intent;

/**
 * @author googolmo
 * 
 */
public class BaseActivity extends Activity {

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	@Override
	public void finish() {

		super.finish();

	}

	/**
	 * 显示登陆界面
	 */
	public void showLogin() {
		Intent loginIntent = new Intent(this, OAuthAcitivity.class);
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(loginIntent);

	}

}
