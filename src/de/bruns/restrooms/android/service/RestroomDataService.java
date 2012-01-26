package de.bruns.restrooms.android.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.location.Location;
import de.bruns.restrooms.android.Configuration;
import de.bruns.restrooms.android.data.RestroomData;

/**
 * Android depnding service for handling the restroom data.
 */
public class RestroomDataService {

	public static final RestroomDataService INSTANCE = new RestroomDataService();

	private List<RestroomData> restrooms;
	private Location currentLoaction;

	private RestroomDataService() {
		RestroomDataProvider restroomDataProvider = new RestroomDataProvider();
		restrooms = restroomDataProvider.getRestroomData();
		
		Location initialLocation = new Location((String) null);
		initialLocation.setLatitude(Configuration.getCurrentLatitude());
		initialLocation.setLongitude(Configuration.getCurrentLongitude());
		
		updateLocation(initialLocation);
	}

	public void updateLocation(Location currentLoaction) {
		this.currentLoaction = currentLoaction;

		calculateDistances();
	}

	private void calculateDistances() {
		Location locationRestroom = new Location((String) null);
		for (RestroomData restroomData : restrooms) {
			locationRestroom.setLatitude(restroomData.getLatitude());
			locationRestroom.setLongitude(restroomData.getLongitude());
			restroomData.setDistanceInMeter((int) currentLoaction
					.distanceTo(locationRestroom));
		}

		Collections.sort(restrooms, new Comparator<RestroomData>() {
			@Override
			public int compare(RestroomData data1, RestroomData data2) {
				return data1.getDistanceInMeter() -  data2.getDistanceInMeter();
			}
		});
	}

	public List<RestroomData> getRestrooms() {
		return restrooms;
	}

	public RestroomData getRestroomForName(String restroomId) {
		for (RestroomData restroom : restrooms) {
			if (restroom.getName().equals(restroomId)) {
				return restroom;
			}
		}
		return null;
	}
}
