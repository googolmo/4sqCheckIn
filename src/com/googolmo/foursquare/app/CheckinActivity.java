/**
 * 
 */
package com.googolmo.foursquare.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.googolmo.foursquare.BaseActivity;
import com.googolmo.foursquare.CheckInApplication;
import com.googolmo.foursquare.R;
import com.googolmo.foursquare.utils.LogUtil;

import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.Checkin;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;

/**
 * @author googolmo
 * 
 */
public class CheckinActivity extends BaseActivity {

	private LogUtil log = new LogUtil(CheckinActivity.class.getName());

	private EditText mEditTextShout;
	private TextView mShoutCharactersLeft;
	private Button mBtnShareWithFriendsYes;
	private Button mBtnShareWithFriendsNo;
	private Button mBtnShareFacebook;
	private Button mBtnShareTwitter;
	private Button mBtnCheckin;

	private ProgressDialog mProgressDialog;

	private boolean mIsShared;
	private boolean mIsSharedFB;
	private boolean mIsSharedTwitter;

	private Bundle mBundle;
	private String mVenueId;
	private String mVenueName;
	private String mVenueLL;

	private Thread mThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkin_or_shout_gather_info_activity);
		this.initData();
		setupView();
		setTitle(mVenueName);

	}

	private void setupView() {
		mEditTextShout = (EditText) findViewById(R.id.editTextShout);
		mShoutCharactersLeft = (TextView) findViewById(R.id.shoutCharactersLeft);
		mBtnShareWithFriendsYes = (Button) findViewById(R.id.btnShareWithFriendsYes);
		mBtnShareWithFriendsNo = (Button) findViewById(R.id.btnShareWithFriendsNo);
		mBtnShareFacebook = (Button) findViewById(R.id.btnShareFacebook);
		mBtnShareTwitter = (Button) findViewById(R.id.btnShareTwitter);
		mBtnCheckin = (Button) findViewById(R.id.btnCheckin);

		mBtnShareWithFriendsYes.setOnTouchListener(mShareOnTouchListener);
		mBtnShareWithFriendsNo.setOnTouchListener(mShareOnTouchListener);
		mBtnShareFacebook.setOnTouchListener(mShareOnTouchListener);
		mBtnShareTwitter.setOnTouchListener(mShareOnTouchListener);

		// mBtnShareWithFriendsYes.setOnClickListener(mShareOnClickListener);
		// mBtnShareWithFriendsNo.setOnClickListener(mShareOnClickListener);
		// mBtnShareFacebook.setOnClickListener(mShareOnClickListener);
		// mBtnShareTwitter.setOnClickListener(mShareOnClickListener);

		mBtnCheckin.setOnClickListener(mCheckInOnClickListener);

		setShareWithFriends(true);
		mBtnShareFacebook.setBackgroundResource(R.drawable.modal_fb_on);
		mBtnShareTwitter.setBackgroundResource(R.drawable.modal_twitter_on);
	}

	private void initData() {
		mBundle = getIntent().getExtras().getBundle("bundle");
		this.mVenueId = mBundle.getString("venueId");
		this.mVenueName = mBundle.getString("venueName");
		this.mVenueLL = mBundle.getDouble("lat") + ","
				+ mBundle.getDouble("lng");
		log.debug(mVenueLL);

		mIsShared = true;
		mIsSharedFB = true;
		mIsSharedTwitter = true;
	}

	private View.OnTouchListener mShareOnTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.getId() == mBtnShareWithFriendsYes.getId()) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mBtnShareWithFriendsYes
							.setBackgroundResource(R.drawable.yes_toggle_pressed);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_MASK:
					setShareWithFriends(true);
					break;

				}
			} else if (v.getId() == mBtnShareWithFriendsNo.getId()) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mBtnShareWithFriendsNo
							.setBackgroundResource(R.drawable.no_toggle_pressed);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_MASK:
					setShareWithFriends(false);
					break;
				}
			} else if (v.getId() == mBtnShareFacebook.getId() && mIsShared) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mBtnShareFacebook
							.setBackgroundResource(R.drawable.modal_fb_pressed);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_MASK:
					setShareFB();
					break;
				}
			} else if (v.getId() == mBtnShareTwitter.getId() && mIsShared) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mBtnShareTwitter
							.setBackgroundResource(R.drawable.modal_twitter_pressed);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_MASK:
					setShareTwitter();
					break;
				}
			}
			return false;
		}
	};

	// private View.OnClickListener mShareOnClickListener = new
	// View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// if (v.getId() == mBtnShareWithFriendsYes.getId()) {
	//
	// setShareWithFriends(true);
	//
	// } else if (v.getId() == mBtnShareWithFriendsNo.getId()) {
	//
	// setShareWithFriends(false);
	//
	// } else if (v.getId() == mBtnShareFacebook.getId() && mIsShared) {
	//
	// setShareFB();
	//
	// } else if (v.getId() == mBtnShareTwitter.getId() && mIsShared) {
	//
	// setShareTwitter();
	// }
	//
	// }
	// };

	private View.OnClickListener mCheckInOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			mProgressDialog = new ProgressDialog(CheckinActivity.this);
			mProgressDialog.setMessage("checkin");
			mProgressDialog.show();

			mThread = new Thread(mCheckinRunnable);
			mThread.start();
		}
	};

	private void setShareWithFriends(boolean value) {
		if (value) {
			mBtnShareWithFriendsYes
					.setBackgroundResource(R.drawable.yes_toggle_on);
			mBtnShareWithFriendsNo
					.setBackgroundResource(R.drawable.no_toggle_off);
			if (mIsSharedFB) {
				mBtnShareFacebook.setBackgroundResource(R.drawable.modal_fb_on);
			} else {
				mBtnShareFacebook
						.setBackgroundResource(R.drawable.modal_fb_off);
			}

			if (mIsSharedTwitter) {
				mBtnShareTwitter
						.setBackgroundResource(R.drawable.modal_twitter_on);
			} else {
				mBtnShareTwitter
						.setBackgroundResource(R.drawable.modal_twitter_off);
			}
		} else {
			mBtnShareWithFriendsYes
					.setBackgroundResource(R.drawable.yes_toggle_off);
			mBtnShareWithFriendsNo
					.setBackgroundResource(R.drawable.no_toggle_on);
			mBtnShareFacebook
					.setBackgroundResource(R.drawable.modal_fb_disabled);
			mBtnShareTwitter
					.setBackgroundResource(R.drawable.modal_twitter_disabled);
			mIsSharedFB = false;
			mIsSharedTwitter = false;
		}
		mIsShared = value;
	}

	private void setShareFB() {
		boolean value = mIsSharedFB;
		if (!value) {
			mBtnShareFacebook.setBackgroundResource(R.drawable.modal_fb_on);

		} else {
			mBtnShareFacebook.setBackgroundResource(R.drawable.modal_fb_off);
		}
		mIsSharedFB = !value;
	}

	private void setShareTwitter() {
		boolean value = mIsSharedTwitter;
		if (!value) {
			mBtnShareTwitter.setBackgroundResource(R.drawable.modal_twitter_on);
		} else {
			mBtnShareTwitter
					.setBackgroundResource(R.drawable.modal_twitter_off);
		}
		mIsSharedTwitter = !value;
	}

	private String getBroadcastString() {
		String value = "public";
		if (!mIsShared) {
			value = "private";
		} else {
			value = "public";
			if (mIsSharedFB) {
				value += ",facebook";
			}
			if (mIsSharedTwitter) {
				value += ",twitter";
			}
		}
		return value;
	}

	public Runnable mCheckinRunnable = new Runnable() {

		@Override
		public void run() {
			Message message = mHandler.obtainMessage();
			try {

				String shout = null;

				if (mEditTextShout.getText().toString().length() > 0) {
					shout = mEditTextShout.getText().toString();
				}

				Result<Checkin> result = ((CheckInApplication) getApplication())
						.getApi().checkinsAdd(mVenueId, null, shout,
								getBroadcastString(), mVenueLL, null, null,
								null);
				if (result.getMeta().getCode() == 200) {
					log.debug("成功:" + result.getMeta().getCode());
					message.what = 1;
					message.obj = result;
				} else {
					log.debug("失败:" + result.getMeta().getCode());
					message.what = 2;
					message.obj = result.getMeta().getErrorDetail();
				}
			} catch (FoursquareApiException e) {
				message.what = 0;
				message.obj = e.getMessage();
				e.printStackTrace();
			}
			mHandler.sendMessage(message);

		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mProgressDialog.cancel();
			switch (msg.what) {
			case 0:
			case 2:
				log.debug(msg.obj.toString());
				Toast.makeText(CheckinActivity.this, msg.obj.toString(),
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				log.debug("成功");
				Toast.makeText(CheckinActivity.this, "成功!", Toast.LENGTH_SHORT)
						.show();

				//
				finish();
				// updateListView((Result<VenuesSearchResult>) msg.obj);
				break;
			}
			super.handleMessage(msg);
		}

	};

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
