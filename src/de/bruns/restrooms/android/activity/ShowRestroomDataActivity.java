package de.bruns.restrooms.android.activity;

import java.util.Date;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import de.bruns.restrooms.android.DrawableFactory;
import de.bruns.restrooms.android.IntentFactory;
import de.bruns.restrooms.android.R;
import de.bruns.restrooms.android.data.RestroomData;
import de.bruns.restrooms.android.service.CurrentPositionService;
import de.bruns.restrooms.android.service.RestroomDataService;
import de.bruns.restrooms.android.service.TimeService;

public class ShowRestroomDataActivity extends MapActivity {

	private static final String LOG_TAG = ShowRestroomDataActivity.class
			.getSimpleName();

	private static final String LINE_SEPERATOR = "\n";
	public static final String RESTROOM_ID = "RESTROOM_ID";
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final GeoPoint currentPosition = CurrentPositionService.instance(this).getCurrentPosition();;
		
		setContentView(R.layout.show_restroom_data);
		final Bundle extras = getIntent().getExtras();

		String restroomId = extras
				.getString(ShowRestroomDataActivity.RESTROOM_ID);
		setTitle(getTitle() + " " + restroomId);
		Log.v(LOG_TAG, "Selected restroom: " + restroomId);

		RestroomData restroom = RestroomDataService.instance(this)
				.getRestroomForName(restroomId);
		
		String addressContent = 
				restroom.getName() + LINE_SEPERATOR + //
				restroom.getDistanceAsString() + " - " +restroom.getAdresse() + LINE_SEPERATOR + //
				restroom.getLatitude() + " / " + restroom.getLongitude();
		((TextView) findViewById(R.id.txt_address)).setText(addressContent);
		
		String openingHoursContent = restroom
				.getOpeningHours().replace(", ", LINE_SEPERATOR).replace(",", LINE_SEPERATOR);
		((TextView) findViewById(R.id.txt_openinghours)).setText(openingHoursContent);
		
		final GeoPoint restroomLocation = ShowRestroomsMapActivity.getGeoPoint(restroom);

		// map view
		MapView mapView = (MapView) findViewById(R.id.map_restroom_location);
		mapView.setBuiltInZoomControls(true);

		MapController mapController = mapView.getController();
		
		int zoom = CurrentPositionService.getZoomForDistance(restroom.getDistanceInMeter());
		mapController.setZoom(zoom);
		mapController.setCenter(restroomLocation);

		Date currentTime = TimeService.instance().getTime();

		// restroom location overlay
		Drawable restroomLocationDrawable = DrawableFactory.getRestroomMarker(this, restroom, currentTime);
		SingleItemOverlay restroomLocationOverlay = new SingleItemOverlay(this,
				restroomLocationDrawable, restroomLocation, restroom.getName());
		mapView.getOverlays().add(restroomLocationOverlay);

		// my location overlay
		SingleItemOverlay myPositionOverlay = new SingleItemOverlay(this, this
				.getResources().getDrawable(R.drawable.you_are_here),currentPosition, "Mein Standort");
		mapView.getOverlays().add(myPositionOverlay);
		
		// buttons apps
		Button buttonStreetview = (Button) findViewById(R.id.button_streetview);
		buttonStreetview.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = IntentFactory.createStreetViewIntent(restroomLocation);
				startActivity(intent);
				return true;
			}
		});
		Button buttonNavigation = (Button) findViewById(R.id.button_navigation);
		buttonNavigation.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = IntentFactory.createNavigationIntent(restroomLocation);
				startActivity(intent);
				return true;
			}
		});
		Button buttonMaps = (Button) findViewById(R.id.button_maps);
		buttonMaps.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = IntentFactory.createMapsIntent(currentPosition, restroomLocation);
				startActivity(intent);
				return true;
			}
		});
		
		// buttons back
		Button buttonAsList = (Button) findViewById(R.id.button_aslist);
		buttonAsList.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				startActivity(new Intent(ShowRestroomDataActivity.this, ShowRestroomsListActivity.class));
				return true;
			}
		});
		Button buttonAsMap = (Button) findViewById(R.id.button_asmap);
		buttonAsMap.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				startActivity(new Intent(ShowRestroomDataActivity.this, ShowRestroomsMapActivity.class));
				return true;
			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
