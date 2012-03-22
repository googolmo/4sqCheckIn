/**
 * 
 */
package com.googolmo.foursquare;

import com.googolmo.foursquare.utils.Configuration;

import fi.foyt.foursquare.api.FoursquareApi;
import android.app.Application;

/**
 * @author googolmo
 * 
 */
public class CheckInApplication extends Application {

	public static FoursquareApi api;

	public FoursquareApi getApi() {
		if (api == null) {
			api = new FoursquareApi(Configuration.CLIENTID,
					Configuration.CLIENTSECRET, Configuration.REDIRECTURL);
		}
		return api;
	}

	/**
	 * 
	 */
	public CheckInApplication() {
		// TODO Auto-generated constructor stub
	}

}
