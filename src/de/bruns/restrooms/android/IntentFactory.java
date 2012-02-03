/*
 * Copyright 2012 Andreas Bruns
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
