/**
 * 
 */
package com.googolmo.foursquare.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * @author googolmo
 * 
 */
public class PreferenceUtil {

	public static String KEY_4SQCHECKIN_CODE = "code";
	public static String KEY_4SQCHECKIN_OAUTHTOKEN = "authToken";
	public static String KEY_4SQCHECKIN_LASTOAUTHTIME = "lastOAuthTime";

	/**
	 * 获得CODE
	 * 
	 * @param context
	 * @return
	 */
	public static String getCode(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(KEY_4SQCHECKIN_CODE, "");
	}

	/**
	 * 设置CODE
	 * 
	 * @param context
	 * @param code
	 */
	public static void setCode(Context context, String code) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString(KEY_4SQCHECKIN_CODE, code);
		editor.commit();
	}

	/**
	 * 获得OAuthToken
	 * 
	 * @param context
	 * @return
	 */
	public static String getOAuthToken(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(KEY_4SQCHECKIN_OAUTHTOKEN, "");
	}

	/**
	 * 存入 OAuthToken
	 * 
	 * @param context
	 * @param value
	 */
	public static void setOAuthToken(Context context, String value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString(KEY_4SQCHECKIN_OAUTHTOKEN, value);
		editor.putLong(KEY_4SQCHECKIN_LASTOAUTHTIME, System.currentTimeMillis());
		editor.commit();
	}

	public static long getLastOAuthTime(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getLong(KEY_4SQCHECKIN_LASTOAUTHTIME, 0);
	}

	public static void setLastOAuthTime(Context context, long value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putLong(KEY_4SQCHECKIN_LASTOAUTHTIME, value);
		editor.commit();
	}
}
