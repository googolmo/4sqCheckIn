package com.googolmo.foursquare;

import java.nio.channels.SelectableChannel;

import com.googolmo.foursquare.app.DefaultActivity;
import com.googolmo.foursquare.app.OAuthAcitivity;
import com.googolmo.foursquare.utils.LogUtil;
import com.googolmo.foursquare.utils.PreferenceUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity {

	private LogUtil log = LogUtil.getLog(this.getClass().getName());

	private Button buttonOAuth;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String code = PreferenceUtil.getCode(this);
		if (!code.equals("")) {
			log.debug(code);
			Intent intent = new Intent(this, DefaultActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			intent.putExtra(PreferenceUtil.KEY_4SQCHECKIN_CODE, code);

			startActivity(intent);
			finish();

		} else {
			setupView();
		}

		// setContentView(R.layout.main);
		//
		//
		//
		// FoursquareApi foursquareApi = new FoursquareApi(
		// Configuration.getClientID(), Configuration.getClientSecret(),
		// RedirectUrl);
		// String url = foursquareApi.getAuthenticationUrl();
		//
		// // oauthWebView = (WebView) findViewById(R.id.webview_oauth);
		// // oauthWebView.getSettings().setJavaScriptEnabled(true);
		// // oauthWebView.setWebViewClient(new WebViewClient() {
		// //
		// // @Override
		// // public void onPageStarted(WebView view, String url, Bitmap
		// favicon) {
		// // // TODO Auto-generated method stub
		// // super.onPageStarted(view, url, favicon);
		// // String fragment = "code=";
		// // int start = url.indexOf(fragment);
		// // Log.d("34", start + "");
		// // if (start > -1) {
		// // // You can use the accessToken for api calls now.
		// // String accessToken = url.substring(
		// // start + fragment.length(), url.length());
		// //
		// // Log.d("34", "OAuth complete, token: [" + accessToken + "].");
		// // Toast.makeText(mainActivity.this, accessToken,
		// // Toast.LENGTH_SHORT).show();
		// // }
		// // }
		// //
		// // });
		// // oauthWebView.loadUrl(url);
		//
		// // foursquareApi.authenticateCode(code)

	}

	private void setupView() {

		buttonOAuth = (Button) findViewById(R.id.button_oauth);

		buttonOAuth.setOnClickListener(oAuthClickListener);
	}

	private View.OnClickListener oAuthClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, OAuthAcitivity.class);
			startActivity(intent);
		}
	};

}