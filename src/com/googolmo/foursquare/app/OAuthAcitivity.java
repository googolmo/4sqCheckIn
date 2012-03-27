/**
 * 
 */
package com.googolmo.foursquare.app;

import com.googolmo.foursquare.CheckInApplication;
import com.googolmo.foursquare.MainActivity;
import com.googolmo.foursquare.R;
import com.googolmo.foursquare.BaseActivity;
import com.googolmo.foursquare.utils.PreferenceUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author googolmo
 * 
 */
public class OAuthAcitivity extends BaseActivity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oauth);

		webView = (WebView) findViewById(R.id.webview_oauth);

		CheckInApplication app = (CheckInApplication) getApplication();
		String url = app.getApi().getAuthenticationTokenUrl();

		webView = (WebView) findViewById(R.id.webview_oauth);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				String fragment = "#access_token=";
				int start = url.indexOf(fragment);

				if (start > -1) {
					// You can use the accessToken for api calls now.
					String code = url.substring(start + fragment.length(),
							url.length());
					// PreferenceUtil.setCode(OAuthAcitivity.this, code);
					PreferenceUtil.setOAuthToken(OAuthAcitivity.this, code);

					Intent intent = new Intent(OAuthAcitivity.this,
							MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra(PreferenceUtil.KEY_4SQCHECKIN_CODE, code);
					startActivity(intent);
					finish();
				}

			}

		});
		webView.loadUrl(url);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
