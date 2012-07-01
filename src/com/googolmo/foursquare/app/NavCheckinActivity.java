/**
 * 
 */
package com.googolmo.foursquare.app;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.googolmo.foursquare.CheckInApplication;
import com.googolmo.foursquare.Constants;
import com.googolmo.foursquare.R;
import com.googolmo.foursquare.utils.AsyncImageLoader;
import com.googolmo.foursquare.utils.LogUtil;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author googolmo
 * 
 */
public class NavCheckinActivity extends SherlockFragment {

	LogUtil mLog = LogUtil.getLog(NavCheckinActivity.class.getName());

	LinearLayout mViewNetworkProviderOff;
	LinearLayout mViewListview;
	ScrollView mViewNoLocationProviders;
	LinearLayout mEmptyLoadingLayout;

	ListView mVenueListView;

	private LocationManager mLocationManager;
	private Location mLocation;

    private VenuesSearchResult mVenuesSearchResult;

	private List<AsyncTask> mTaskList = new ArrayList<AsyncTask>();

	private static final int TWO_MINUTES = 1000 * 60 * 2;

    private static NavCheckinActivity fragment;

    public static NavCheckinActivity getinstance(){
         if (fragment == null) {
             fragment = new NavCheckinActivity();
         }
        return fragment;
    }


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        setContentView(R.layout.nav_checkin);
        mTaskList.clear();
        setHasOptionsMenu(true);
        mLog.debug("onCreate");
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mLocation = new Location(LocationManager.NETWORK_PROVIDER);
            if (savedInstanceState.containsKey("location.lat")) {
                mLocation.setLatitude(savedInstanceState.getDouble("location.lat"));
            }
            if (savedInstanceState.containsKey("location.lng")) {
                mLocation.setLongitude(savedInstanceState.getDouble("location.lng"));
            }
            if (savedInstanceState.containsKey("venues")) {
                mVenuesSearchResult = (VenuesSearchResult)savedInstanceState.getSerializable("venues");
            }

        }
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mLocation != null) {
            outState.putDouble("location.lat", mLocation.getLatitude());
            outState.putDouble("location.lng", mLocation.getLongitude());
        }
        if (mVenuesSearchResult != null) {
            outState.putSerializable("venues", mVenuesSearchResult);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        container.removeAllViews();
        View v = inflater.inflate(R.layout.nav_checkin, container, false);

        setupView(v);
        if (mVenuesSearchResult == null) {
            mViewNoLocationProviders.setVisibility(View.GONE);
            mVenueListView.setVisibility(View.GONE);
            this.getLocation();
        } else {
            mViewNoLocationProviders.setVisibility(View.GONE);
            mVenueListView.setVisibility(View.VISIBLE);
            updateListView(mVenuesSearchResult);
        }

        return v;
    }

    private void setupView(View v) {
		mViewNetworkProviderOff = (LinearLayout) v.findViewById(R.id.viewNetworkProviderOff);
		mViewListview = (LinearLayout) v.findViewById(R.id.viewListview);
		mViewNoLocationProviders = (ScrollView) v.findViewById(R.id.viewNoLocationProviders);
		mEmptyLoadingLayout = (LinearLayout) v.findViewById(R.id.empty_loading_layout);
		mVenueListView = (ListView) v.findViewById(R.id.listview);
	}

	private void getLocation() {

		// mLocationManagerProxy = new LocationManagerProxy(this,
		// Configuration.MAPABC_APIKEY);
		//
		// for (final String provider :
		// mLocationManagerProxy.getProviders(true)) {
		// if (LocationManagerProxy.GPS_PROVIDER.equals(provider)
		// || LocationProviderProxy.MapABCNetwork.equals(provider)) {
		// mLocationManagerProxy.requestLocationUpdates(provider, 0, 0,
		// mLocationListener);
		// }
		// }

		if (mLocationManager == null) {
			mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		}

		mLocation = mLocationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (mLocation == null) {
			mLocation = mLocationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		// updateLocation(mLocation);

		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				0, 0, mLocationListener);
		mLocationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
	}

	private void updateLocation(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			mLocation = location;
            mLog.debug("lat:" + lat + " lng:" + lng);
		} else {
            mLog.debug("Can't access your location");
            Toast.makeText(this.getSherlockActivity(), "Can't access your location", Toast.LENGTH_LONG).show();
		}
		mLocationManager.removeUpdates(mLocationListener);

        String ll = location.getLatitude() + ","
                + location.getLongitude();

        VenueSearchAsyncTask venueTask = new VenueSearchAsyncTask();
        venueTask.execute(ll);

        mTaskList.add(venueTask);

	}


	private final LocationListener mLocationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
            mLog.info("Provider(" + provider + ") now is enabled");
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
            mLog.warn("Provider(" + provider + ") now is disabled");
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if (isBetterLocation(location, mLocation)) {
				updateLocation(location);
			}
		}
	};

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	private void updateListView(final VenuesSearchResult result) {
		this.mEmptyLoadingLayout.setVisibility(View.GONE);
		this.mViewListview.setVisibility(View.VISIBLE);

        mLog.debug(result.getVenues().length);

		VenueAdapter venueAdapter = new VenueAdapter(getActivity(), result);
		this.mVenueListView.setAdapter(venueAdapter);
		this.mVenueListView.setVisibility(View.VISIBLE);
		this.mVenueListView.invalidate();
		this.mVenueListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						// result.getResult().getVenues()[arg2]
                        mLog.debug("click at" + arg2);
						Bundle bundle = new Bundle();

						bundle.putString("venueId", result.getVenues()[arg2].getId());
						bundle.putString("venueName", result.getVenues()[arg2].getName());
						bundle.putDouble("lat",
								result.getVenues()[arg2].getLocation().getLat());
						bundle.putDouble("lng",
								result.getVenues()[arg2].getLocation().getLng());

						Intent intent = new Intent(getActivity(),
								CheckinActivity.class);
						intent.putExtra("bundle", bundle);
                        intent.putExtra(Constants.KEY_VENUE, result.getVenues()[arg2]);
						startActivity(intent);
					}
				});

	}

	/**
	 * 获得地址字符串
	 * 
	 * @param location
	 * @return
	 */
	private String getAddress(fi.foyt.foursquare.api.entities.Location location) {
		String rst = null;
		if (location.getAddress() != null) {
			rst = location.getAddress();
			if (location.getCrossStreet() != null) {
				rst = rst + "(" + location.getCrossStreet() + ")";
			}
		} else {
			if (location.getCity() != null) {
				rst = location.getCity();
			}
			if (location.getState() != null) {
				if (rst != null) {
					rst = rst + "," + location.getState();
				} else {
					rst = location.getState();
				}

			}
			if (location.getCountry() != null) {
				if (rst != null) {
					rst = rst + "," + location.getCountry();
				} else {
					rst = location.getCountry();
				}
			}
		}
		return rst;
	}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
		case Menu.FIRST:
            mLog.debug("刷新");
			getLocation();
			break;
		case Menu.FIRST + 1:
            mLog.debug("设置");
			break;
		}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.add(Menu.NONE, Menu.FIRST, Menu.FIRST, R.string.refresh);
        menu.add(Menu.NONE, Menu.FIRST + 1, Menu.FIRST + 1, R.string.configure);
        super.onCreateOptionsMenu(menu, inflater);
    }



//	@Override
//	public void finish() {
//		// TODO Auto-generated method stub
//		super.finish();
//	}

    @Override
    public void onPause() {
        super.onPause();
        try {
			mLocationManager.removeUpdates(mLocationListener);
			// mLocationManagerProxy.removeUpdates(mLocationListener);
		} catch (Exception e) {
            mLog.warn("mLocationManager has removeUpdates");
		}
    }


//	@Override
//	public void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		try {
//			mLocationManager.removeUpdates(mLocationListener);
//			// mLocationManagerProxy.removeUpdates(mLocationListener);
//		} catch (Exception e) {
//			log.warn("mLocationManager has removeUpdates");
//		}
//
//	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

    class VenueSearchAsyncTask extends AsyncTask<String, Void, Result<VenuesSearchResult>> {

        @Override
        protected Result<VenuesSearchResult> doInBackground(String... params) {

            if (params != null && params.length > 0) {
                try {
                    return ((CheckInApplication) getActivity().getApplication())
                            .getApi().venuesSearch(params[0], null, null, null, null, 50,
                                    "checkin", null, null, null, null);
                } catch (FoursquareApiException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Result<VenuesSearchResult> result) {
            if(result != null) {
                if (result.getMeta().getCode() == 200) {
                    mVenuesSearchResult = result.getResult();
                    updateListView(result.getResult());
                } else {
                    Toast.makeText(getActivity(), result.getMeta().getErrorDetail(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "出现了点错误!", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }

	public class VenueAdapter extends BaseAdapter {

		LayoutInflater mInflater;
		Context mContext;
		VenuesSearchResult mResult;

		public VenueAdapter(Context context) {
			this.mContext = context;
			mInflater = LayoutInflater.from(context);
		}

		public VenueAdapter(Context context, VenuesSearchResult result) {
			this.mContext = context;
			this.mResult = result;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mResult.getVenues().length;
		}

		@Override
		public Object getItem(int position) {
			if (position <= getCount()) {
				return mResult.getVenues()[position];
			} else {
				return null;
			}

		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.venue_list_item, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.venueName = (TextView) convertView
						.findViewById(R.id.venueName);
				holder.venueLocationLine1 = (TextView) convertView
						.findViewById(R.id.venueLocationLine1);
				holder.venueDistance = (TextView) convertView
						.findViewById(R.id.venueDistance);
				holder.venueCheckinCount = (TextView) convertView
						.findViewById(R.id.venueCheckinCount);
				holder.venueEventSummary = (TextView) convertView
						.findViewById(R.id.venueEventSummary);
				holder.iconEvent = (ImageView) convertView
						.findViewById(R.id.iconEvent);
				holder.iconSpecialHere = (ImageView) convertView
						.findViewById(R.id.iconSpecialHere);
				holder.venueTodoCorner = (ImageView) convertView
						.findViewById(R.id.venueTodoCorner);
				holder.iconTrending = (ImageView) convertView
						.findViewById(R.id.iconTrending);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}

			CompactVenue venue = this.mResult.getVenues()[position];
            mLog.debug("position = " + position + ";venue =" + venue.getName()
					+ ";lat=" + venue.getLocation().getLat());
			holder.venueName.setText(venue.getName());
			String address = getAddress(venue.getLocation());
			if (null != address) {
				holder.venueLocationLine1.setText(venue.getLocation()
						.getAddress());
			} else {
				holder.venueLocationLine1.setVisibility(View.GONE);
			}
			holder.venueDistance.setText(venue.getLocation().getDistance()
					+ " " + getString(R.string.meters));
			if (venue.getHereNow() == null || venue.getHereNow().getCount() < 1) {
				holder.iconTrending.setVisibility(View.GONE);
				holder.venueCheckinCount.setVisibility(View.GONE);
			} else {
				holder.venueCheckinCount.setText(venue.getHereNow().getCount()
						+ "");
			}

			if (venue.getSpecials() == null || venue.getSpecials().length < 1) {
				holder.iconSpecialHere.setVisibility(View.GONE);
			}
            if (venue.getCategories().length > 0) {
                String iconUrl = venue.getCategories()[0].getIcon().replace(".png", "_256.png");
                AsyncImageLoader.loadImage(convertView.getContext(), iconUrl, holder.icon, R.drawable.category_none, true);
            }


			return convertView;
		}

	}

	static class ViewHolder {
		ImageView icon;
		TextView venueName;
		TextView venueLocationLine1;
		TextView venueDistance;
		TextView venueCheckinCount;
		ImageView iconEvent;
		ImageView iconTrending;
		TextView venueEventSummary;
		ImageView iconSpecialHere;
		ImageView venueTodoCorner;
	}

    @Override
    public void onDestroy() {
        if (mTaskList != null) {
            for (AsyncTask task : mTaskList) {
                if (!task.getStatus().equals(AsyncTask.Status.FINISHED)) {
                    task.cancel(true);
                }
            }
        }
        super.onDestroy();
    }
}
