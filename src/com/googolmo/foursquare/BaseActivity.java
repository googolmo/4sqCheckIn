/**
 * 
 */
package com.googolmo.foursquare;

import android.os.AsyncTask;
import android.os.Bundle;
import com.googolmo.foursquare.app.OAuthAcitivity;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author googolmo
 * 
 */
public class BaseActivity extends Activity {

    public List<AsyncTask> mTaskList = new ArrayList<AsyncTask>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mTaskList.clear();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (this.mTaskList != null) {
            for (AsyncTask task : mTaskList) {
                if (! task.getStatus().equals(AsyncTask.Status.FINISHED)) {
                    task.cancel(true);
                }
            }
        }
        super.onDestroy();
    }

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
