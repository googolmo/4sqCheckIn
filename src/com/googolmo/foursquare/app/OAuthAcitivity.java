/**
 * 
 */
package com.googolmo.foursquare.app;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
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
public class OAuthAcitivity extends SherlockActivity {

	private WebView mWebView;
    private String mAccessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oauth);
        getSupportActionBar();
        setSupportProgressBarVisibility(true);

        mWebView = (WebView) findViewById(R.id.webview_oauth);

		CheckInApplication app = (CheckInApplication) getApplication();
		String url = app.getApi().getAuthenticationTokenUrl();

        mWebView = (WebView) findViewById(R.id.webview_oauth);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

			}

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String fragment = "#access_token=";
                int start = url.indexOf(fragment);

                if (start > -1) {
                    view.stopLoading();
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
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mWebView.loadUrl(url);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
