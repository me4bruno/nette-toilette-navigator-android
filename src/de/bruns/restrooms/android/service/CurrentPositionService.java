package de.bruns.restrooms.android.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class CurrentPositionService {

	public static interface CurrentPositionListener {
		public void positionUpdated(GeoPoint currentPosition);
	}

	private static final String LOG_TAG = CurrentPositionService.class
			.getSimpleName();
	private static final GeoPoint GEO_POINT_BREMEN = new GeoPoint(
			(int) (53.075813 * 1E6), (int) (8.807357 * 1E6));

	private GeoPoint currentPosition;
	private GeoPoint currentGpsPosition;
	private boolean useGps;

	private List<CurrentPositionListener> currentPositionListeners;
	private final Geocoder geocoder;

	public CurrentPositionService(LocationManager locationManager,
			Geocoder geocoder) {
		currentPosition = GEO_POINT_BREMEN;
		currentGpsPosition = GEO_POINT_BREMEN;
		useGps = true;
		currentPositionListeners = new ArrayList<CurrentPositionListener>();

		this.geocoder = geocoder;
		LocationListener locationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
	}

	public String getAddressOfPosition() {
		String addressString = "No location found";
		double lat = currentPosition.getLatitudeE6() / ((double) 1E6);
		double lng = currentPosition.getLongitudeE6() / ((double) 1E6);

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

	public void useGpsPosition() {
		this.useGps = true;
		updateCurrentPosition(currentGpsPosition);
	}

	public void useManualPosition(GeoPoint manualPosition) {
		this.useGps = false;
		updateCurrentPosition(manualPosition);
	}

	public boolean isUseGps() {
		return useGps;
	}

	public void addCurrentPositionListener(CurrentPositionListener listener) {
		currentPositionListeners.add(listener);
		listener.positionUpdated(currentPosition);
	}

	private void updateCurrentPosition(GeoPoint newCurrentPosition) {
		currentPosition = newCurrentPosition;
		for (CurrentPositionListener listener : currentPositionListeners) {
			listener.positionUpdated(currentPosition);
		}
	}

	public GeoPoint getCurrentPosition() {
		return currentPosition;
	}

	public class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {
			int latE6 = (int) (loc.getLatitude() * 1E6);
			int lngE6 = (int) (loc.getLongitude() * 1E6);

			currentGpsPosition = new GeoPoint(latE6, lngE6);
			if (useGps) {
				updateCurrentPosition(currentGpsPosition);
			}

			Log.v(LOG_TAG, "Gps position changed: " + currentGpsPosition);
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.v(LOG_TAG, "Gps disabled: " + provider);
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.v(LOG_TAG, "Gps enabled: " + provider);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.v(LOG_TAG, "Gps status changes: " + status + " - " + provider);
		}
	}
}
