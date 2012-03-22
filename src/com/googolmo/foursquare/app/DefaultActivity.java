/**
 * 
 */
package com.googolmo.foursquare.app;

import com.googolmo.foursquare.R;
import com.googolmo.foursquare.BaseActivity;
import com.googolmo.foursquare.utils.PreferenceUtil;

import android.os.Bundle;
import android.widget.TextView;

/**
 * @author googolmo
 * 
 */
public class DefaultActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.main_app);
		Bundle bundle = getIntent().getExtras();
		String code = bundle.getString(PreferenceUtil.KEY_4SQCHECKIN_CODE);

		TextView textView = (TextView) findViewById(R.id.main_app_text);
		textView.setText(code);

		super.onCreate(savedInstanceState);
	}

}
