package de.bruns.restrooms.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
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

import de.bruns.restrooms.android.R;
import de.bruns.restrooms.android.data.RestroomData;
import de.bruns.restrooms.android.service.CurrentPositionService;
import de.bruns.restrooms.android.service.RestroomDataService;

public class ShowRestroomsMapActivity extends MapActivity {

	// FIXME - show current position

	private static final String LOG_TAG = ShowRestroomDataActivity.class
			.getSimpleName();
	
	private MapView mMapView;
	private MapController mMapController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.show_restrooms_map);

		mMapView = (MapView) findViewById(R.id.show_map_map);
		mMapController = mMapView.getController();

		int maxZoomLevel = mMapView.getMaxZoomLevel();
		mMapController.setZoom(maxZoomLevel - 3);

		mMapView.setBuiltInZoomControls(true); 

		mMapController.animateTo(CurrentPositionService.instance(this).getCurrentPosition());

		// overlay
		RestroomItemizedOverlay restroomOverlay = new RestroomItemizedOverlay(this);
		mMapView.getOverlays().add(restroomOverlay);
		
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
			super(boundCenterBottom(context.getResources().getDrawable(R.drawable.toilets_green)));

			for (RestroomData restroomData : RestroomDataService.instance(context).getRestrooms()) {
				overlayItems.add(new RestroomOverlayItem(restroomData));
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
