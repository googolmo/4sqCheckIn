package com.googolmo.foursquare;

import com.googolmo.foursquare.app.NavExploreActivity;
import com.googolmo.foursquare.app.NavFriendActivity;
import com.googolmo.foursquare.app.NavMeActivity;
import com.googolmo.foursquare.app.OAuthAcitivity;
import com.googolmo.foursquare.utils.LogUtil;
import com.googolmo.foursquare.utils.PreferenceUtil;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactUser;
import fi.foyt.foursquare.api.entities.CompleteUser;
import fi.foyt.foursquare.api.io.IOHandler;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
		setupView();
		log.debug(oauthToken);
		// PreferenceUtil.setOAuthToken(MainActivity.this, oauthToken);
		final FoursquareApi api = ((CheckInApplication) getApplication())
				.getApi();
		api.setoAuthToken(oauthToken);
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// try {
		//
		// Result<CompactUser[]> user = api.usersRequests();
		// if (user == null) {
		// log.debug("is null");
		// }
		// if (user.getResult() == null) {
		// log.error("user result is null "
		// + user.getMeta().getCode());
		// } else {
		// log.debug(user.getResult()[0].getFirstName());
		// }
		// } catch (FoursquareApiException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }).start();

	}

	private void setupView() {
		tabHost = getTabHost();

		Intent intent;

		intent = new Intent(this, NavExploreActivity.class);
		tabSpec = getTabHost()
				.newTabSpec("NavExplore")
				.setIndicator(
						"Explore",
						getResources().getDrawable(
								R.drawable.tab_main_nav_explore_selector))
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