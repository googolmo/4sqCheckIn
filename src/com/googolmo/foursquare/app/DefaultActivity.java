/**
 * 
 */
package com.googolmo.foursquare.app;

import com.googolmo.foursquare.R;
import com.googolmo.foursquare.BaseActivity;
import com.googolmo.foursquare.utils.PreferenceUtil;

import android.os.Bundle;

/**
 * @author googolmo
 * 
 */
public class DefaultActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.act_main);
		Bundle bundle = getIntent().getExtras();
		String code = bundle.getString(PreferenceUtil.KEY_4SQCHECKIN_CODE);

		super.onCreate(savedInstanceState);
	}

}
