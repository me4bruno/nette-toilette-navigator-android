package de.bruns.restrooms.android;

import com.google.android.maps.GeoPoint;

public class Configuration {

	private static final GeoPoint GEO_POINT_BREMEN = new GeoPoint((int) (53.075813 * 1E6), (int) (8.807357 * 1E6));

	private static GeoPoint currentPosition  = GEO_POINT_BREMEN;

	public static GeoPoint getCurrentPosition() {
		return currentPosition;
	}

	public static double getCurrentLatitude() {
		return currentPosition.getLatitudeE6() / 1E6;
	}

	public static double getCurrentLongitude() {
		return currentPosition.getLongitudeE6() / 1E6;
	}
	
	public static void setCurrentPosition(GeoPoint currentPosition) {
		Configuration.currentPosition = currentPosition;
	}
}
