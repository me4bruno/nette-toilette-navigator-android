package de.bruns.restrooms.android;

import android.content.Intent;
import android.net.Uri;

import com.google.android.maps.GeoPoint;

public class IntentFactory {

	public static Intent createStreetViewIntent(GeoPoint endPoint) {
		String endLat = convertE6intToString(endPoint.getLatitudeE6());
		String endLng = convertE6intToString(endPoint.getLongitudeE6());
		
		String uriString = "google.streetview:cbll=" + endLat + "," + endLng;
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
		return intent;
	}

	public static Intent createMapsIntent(GeoPoint startPoint, GeoPoint endPoint) {
		String startLat = convertE6intToString(startPoint.getLatitudeE6());
		String startLng = convertE6intToString(startPoint.getLongitudeE6());
		String endLat = convertE6intToString(endPoint.getLatitudeE6());
		String endLng = convertE6intToString(endPoint.getLongitudeE6());

		String uriString = "http://maps.google.com/maps?saddr=" + startLat
				+ "," + startLng + "&daddr=" + endLat + "," + endLng;
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
		return intent;
	}

	public static Intent createNavigationIntent(GeoPoint endPoint) {
		String endLat = convertE6intToString(endPoint.getLatitudeE6());
		String endLng = convertE6intToString(endPoint.getLongitudeE6());
		
		String uriString = "google.navigation:q=" + endLat + "," + endLng;
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
		return intent;
	}
	
	private static String convertE6intToString(int e6int) {
		return String.valueOf(((double) e6int / 1E6));
	}
}
