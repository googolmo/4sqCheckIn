/**
 * 
 */
package com.googolmo.foursquare.app;

import android.os.Bundle;

import com.googolmo.foursquare.BaseActivity;
import com.googolmo.foursquare.R;
import com.googolmo.foursquare.utils.LogUtil;

/**
 * @author googolmo
 * 
 */
public class NavExploreActivity extends BaseActivity {

	LogUtil log = LogUtil.getLog(NavExploreActivity.class.getName());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav_explore);
		log.debug("onCreate");
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
