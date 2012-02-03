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
package de.bruns.restrooms.android.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.location.Location;
import de.bruns.restrooms.android.data.RestroomData;

public class RestroomDataService {

	private static RestroomDataService instance;

	private List<RestroomData> restrooms;
	private Location currentLoaction;

	private CurrentPositionService currentPositionService;
	
	public static RestroomDataService instance(Context context) {
		if (instance == null) {
			instance = new RestroomDataService(context);
		}
		return instance;
	}
	
	private RestroomDataService(Context context) {
		currentPositionService = CurrentPositionService.instance(context);
		currentLoaction = new Location((String) null);
		
		RestroomDataProvider restroomDataProvider = new RestroomDataProvider();
		restrooms = restroomDataProvider.getRestroomData();
		
		calculateDistances();
	}

	public void calculateDistances() {
		currentLoaction.setLatitude(currentPositionService.getCurrentLatitude());
		currentLoaction.setLongitude(currentPositionService.getCurrentLongitude());
		
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
