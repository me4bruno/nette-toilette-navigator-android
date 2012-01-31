package de.bruns.restrooms.android.service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class CurrentPositionService {

	
	private static final String LOG_TAG = CurrentPositionService.class
			.getSimpleName();

	private static final double BREMEN_LONGITUDE = 8.807357;
	private static final double BREMEN_LATITUDE = 53.075813;
	private static final GeoPoint BREMEN_GEO_POINT = new GeoPoint(
			(int) (BREMEN_LATITUDE * 1E6), (int) (BREMEN_LONGITUDE * 1E6));

	private static CurrentPositionService instance;

	private GeoPoint currentPosition;
	private boolean useGps;
	private final Geocoder geocoder;
	
	public static CurrentPositionService instance(Context context) {
		if (instance == null) {
			instance = new CurrentPositionService(context);
		}
		return instance;
	}
	
	private CurrentPositionService(Context context) {
		geocoder = new Geocoder(context, Locale.getDefault());
		
		currentPosition = BREMEN_GEO_POINT;
		useGps = false;
	}

	public String getAddressOfPosition() {
		String addressString = "Adresse nicht gefunden.";
		double lat = getCurrentLatitude();
		double lng = getCurrentLongitude();

		try {
			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
			StringBuilder sb = new StringBuilder();
			if (addresses.size() > 0) {
				Address address = addresses.get(0);
				for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
					sb.append(address.getAddressLine(i)).append("\n");
				sb.append(address.getCountryName());
			}
			addressString = sb.toString();
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error geocoding a position", e);
		}
		
		Log.v(LOG_TAG, "Address of (" + lat + "," + lng + "): " + addressString);
		return addressString;
	}

	public double getCurrentLongitude() {
		return currentPosition.getLongitudeE6() / ((double) 1E6);
	}

	public double getCurrentLatitude() {
		return currentPosition.getLatitudeE6() / ((double) 1E6);
	}

	public boolean isUseGps() {
		return useGps;
	}

	public void setUseGps(boolean useGps) {
		this.useGps = useGps;
	}

	public GeoPoint getCurrentPosition() {
		return currentPosition;
	}

	public void updateCurrentPosition(GeoPoint currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	public int distanceInMeterToBremenCity() {
		Location myLocation = new Location((String)null);
		myLocation.setLatitude(getCurrentLatitude());
		myLocation.setLongitude(getCurrentLongitude());
		
		Location bremenLocation = new Location((String)null);
		bremenLocation.setLatitude(BREMEN_LATITUDE);
		bremenLocation.setLongitude(BREMEN_LONGITUDE);
		
		return (int) myLocation.distanceTo(bremenLocation);
	}
	
	public static int getZoomForDistance(int distanceInMeter) {
		int zoom = 8;
		if (distanceInMeter  < 50) {
			zoom = 19;
		} else if (distanceInMeter  < 125) {
			zoom = 18;
		} else if (distanceInMeter  < 250) {
			zoom = 17;
		} else if (distanceInMeter  < 500) {
			zoom = 16;
		} else if (distanceInMeter  < 1250) {
			zoom = 15;
		} else if (distanceInMeter  < 2500) {
			zoom = 14;
		} else if (distanceInMeter  < 5000) {
			zoom = 13;
		} else if (distanceInMeter  < 12500) {
			zoom = 12;
		} else if (distanceInMeter  < 25000) {
			zoom = 11;
		} else if (distanceInMeter  < 50000) {
			zoom = 10;
		} else if (distanceInMeter  < 100000) {
			zoom = 9;
		}
		return zoom;
	}

}
