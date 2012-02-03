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
package de.bruns.restrooms.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import de.bruns.restrooms.android.DrawableFactory;
import de.bruns.restrooms.android.R;
import de.bruns.restrooms.android.data.RestroomData;
import de.bruns.restrooms.android.service.CurrentPositionService;
import de.bruns.restrooms.android.service.RestroomDataService;
import de.bruns.restrooms.android.service.TimeService;

public class ShowRestroomsMapActivity extends MapActivity {

	private static final String LOG_TAG = ShowRestroomDataActivity.class
			.getSimpleName();
	
	private MapView mapView;
	private MapController mapController;
	private TimeService timeService;
	private CurrentPositionService currentPositionService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		timeService = TimeService.instance();
		currentPositionService = CurrentPositionService.instance(this);
		
		setContentView(R.layout.show_restrooms_map);

		mapView = (MapView) findViewById(R.id.show_map_map);
		mapController = mapView.getController();

		int distanceInMeterToBremenCity = currentPositionService.distanceInMeterToBremenCity();
		mapController.setZoom(CurrentPositionService.getZoomForDistance(distanceInMeterToBremenCity));

		mapView.setBuiltInZoomControls(true); 

		mapController.animateTo(currentPositionService.getCurrentPosition());

		// overlay
		mapView.getOverlays().add(new RestroomItemizedOverlay(this));
		
		GeoPoint currentPosition = CurrentPositionService.instance(this).getCurrentPosition();
		SingleItemOverlay myPositionOverlay = new SingleItemOverlay(this, this
				.getResources().getDrawable(R.drawable.you_are_here),currentPosition, "Mein Standort");
		mapView.getOverlays().add(myPositionOverlay);
		
		// buttons
		Button buttonAsList = (Button) findViewById(R.id.button_aslist);
		buttonAsList.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				startActivity(new Intent(ShowRestroomsMapActivity.this, ShowRestroomsListActivity.class));
				return true;
			}
		});
		Button buttonPosition = (Button) findViewById(R.id.button_position);
		buttonPosition.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				startActivity(new Intent(ShowRestroomsMapActivity.this, SelectCurrentPositionActivity.class));
				return true;
			}
		});
		Button buttonHelp = (Button) findViewById(R.id.button_help);
		buttonHelp.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				startActivity(new Intent(ShowRestroomsMapActivity.this, ShowHelpActivity.class));
				return true;
			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private class RestroomItemizedOverlay extends ItemizedOverlay<RestroomOverlayItem> {

		private List<RestroomOverlayItem> overlayItems = new ArrayList<RestroomOverlayItem>();

		public RestroomItemizedOverlay(Context context) {
			super(boundCenterBottom(context.getResources().getDrawable(R.drawable.restroom_yellow)));
			
			for (RestroomData restroomData : RestroomDataService.instance(context).getRestrooms()) {
				RestroomOverlayItem restroomOverlayItem = new RestroomOverlayItem(restroomData);
				Drawable marker = DrawableFactory.getRestroomMarker(context,restroomData, timeService.getTime());
				restroomOverlayItem.setMarker(marker);
				overlayItems.add(restroomOverlayItem);
			}

			populate();
		}

		@Override
		protected RestroomOverlayItem createItem(int i) {
			return overlayItems.get(i);
		}

		@Override
		public int size() {
			return overlayItems.size();
		}

		@Override
		protected boolean onTap(int index) {
			RestroomData restroomData = overlayItems.get(index).getRestroomData();
			String restroomId = restroomData.getName();

			Intent intent = new Intent(ShowRestroomsMapActivity.this, ShowRestroomDataActivity.class);
			intent.putExtra(ShowRestroomDataActivity.RESTROOM_ID, restroomId);
			
			Log.v(LOG_TAG, "Selected restroom: " + restroomId);
			startActivity(intent);
			
			return true;
		}
	}

	private class RestroomOverlayItem extends OverlayItem {

		private RestroomData restroomData;

		public RestroomOverlayItem(RestroomData restroomData) {
			super(getGeoPoint(restroomData), restroomData.getName(),
					restroomData.getDescription());
			this.restroomData = restroomData;
		}

		public RestroomData getRestroomData() {
			return restroomData;
		}
	}
	
	public static GeoPoint getGeoPoint(double latitude, double longitude) {
		return new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
	}

	public static GeoPoint getGeoPoint(RestroomData restroomData) {
		return getGeoPoint(restroomData.getLatitude(),restroomData.getLongitude());
	}

}
