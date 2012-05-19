package com.googolmo.foursquare;

import com.googolmo.foursquare.app.NavCheckinActivity;
import com.googolmo.foursquare.app.NavFriendActivity;
import com.googolmo.foursquare.app.NavMeActivity;
import com.googolmo.foursquare.app.OAuthAcitivity;
import com.googolmo.foursquare.utils.LogUtil;
import com.googolmo.foursquare.utils.PreferenceUtil;

import fi.foyt.foursquare.api.FoursquareApi;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

	private LogUtil log = LogUtil.getLog(this.getClass().getName());

	TabHost tabHost;
	TabHost.TabSpec tabSpec;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_app);

		final String oauthToken = PreferenceUtil.getOAuthToken(this);
		if (oauthToken.equals("")) {
			Intent intent = new Intent(MainActivity.this, OAuthAcitivity.class);
			startActivity(intent);
			finish();
		}
		log.debug(oauthToken);
		// PreferenceUtil.setOAuthToken(MainActivity.this, oauthToken);
		final FoursquareApi api = ((CheckInApplication) getApplication())
				.getApi();
		api.setoAuthToken(oauthToken);

		setupView();

	}

	private void setupView() {
		tabHost = getTabHost();

		Intent intent;

		intent = new Intent(this, NavCheckinActivity.class);
		tabSpec = getTabHost()
				.newTabSpec("NavCheckin")
				.setIndicator(
						"Checkin",
						getResources().getDrawable(
								R.drawable.tab_main_nav_checkin_selector))
				.setContent(intent);

		tabHost.addTab(tabSpec);

		intent = new Intent(this, NavFriendActivity.class);
		tabSpec = getTabHost()
				.newTabSpec("NavFriend")
				.setIndicator(
						"Friend",
						getResources().getDrawable(
								R.drawable.tab_main_nav_friends_selector))
				.setContent(intent);
		tabHost.addTab(tabSpec);

		intent = new Intent(this, NavMeActivity.class);
		tabSpec = getTabHost()
				.newTabSpec("NavMe")
				.setIndicator(
						"Me",
						getResources().getDrawable(
								R.drawable.tab_main_nav_me_boy_selector))
				.setContent(intent);
		tabHost.addTab(tabSpec);

		tabHost.setCurrentTab(0);

	}

}