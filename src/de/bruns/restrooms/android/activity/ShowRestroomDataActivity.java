package de.bruns.restrooms.android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import de.bruns.restrooms.android.Configuration;
import de.bruns.restrooms.android.IntentFactory;
import de.bruns.restrooms.android.R;
import de.bruns.restrooms.android.data.RestroomData;
import de.bruns.restrooms.android.service.RestroomDataService;

public class ShowRestroomDataActivity extends MapActivity {

	
	private static final String LOG_TAG = ShowRestroomDataActivity.class
			.getSimpleName();

	private static final String LINE_SEPERATOR = "\n";
	public static final String RESTROOM_ID = "RESTROOM_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RestroomDataService restroomDataService = RestroomDataService.INSTANCE;

		setContentView(R.layout.show_restroom_data);
		final Bundle extras = getIntent().getExtras();

		String restroomId = extras
				.getString(ShowRestroomDataActivity.RESTROOM_ID);
		setTitle(getTitle() + " " + restroomId);
		Log.v(LOG_TAG, "Selected restroom: " + restroomId);

		RestroomData restroom = restroomDataService
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
		
		int zoom = getZoomForDistance(restroom.getDistanceInMeter());
		mapController.setZoom(zoom);
		mapController.setCenter(restroomLocation);

		// restroom location overlay
		Drawable restroomLocationDrawable = null;
		switch (restroom.isOpen()) {
		case RestroomData.TOILET_OPEN:
			restroomLocationDrawable = this.getResources().getDrawable(
					R.drawable.toilets_green);
			break;
		case RestroomData.TOILET_CLOSE:
			restroomLocationDrawable = this.getResources().getDrawable(
					R.drawable.toilets_red);
			break;
		case RestroomData.TOILET_UNCERTAIN:
			restroomLocationDrawable = this.getResources().getDrawable(
					R.drawable.toilets_yellow);
			break;
		default:
			throw new RuntimeException("Unknown isOpen image: "
					+ restroom.isOpen());
		}
		SingleItemOverlay restroomLocationOverlay = new SingleItemOverlay(this,
				restroomLocationDrawable, restroomLocation, restroom.getName());
		mapView.getOverlays().add(restroomLocationOverlay);

		// my location overlay
		SingleItemOverlay myPositionOverlay = new SingleItemOverlay(this, this
				.getResources().getDrawable(R.drawable.marker),
				Configuration.getCurrentPosition(), "Mein Standort");
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
				Intent intent = IntentFactory.createMapsIntent(Configuration.getCurrentPosition(), restroomLocation);
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

	public class SingleItemOverlay extends ItemizedOverlay<OverlayItem> {

		private final Context context;
		private final String toastText;
		private final GeoPoint position;

		public SingleItemOverlay(Context context, Drawable marker,
				GeoPoint position, String toastText) {
			super(boundCenterBottom(marker));
			this.context = context;
			this.position = position;
			this.toastText = toastText;
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return new OverlayItem(position, null, null);
		}

		@Override
		public int size() {
			return 1;
		}

		@Override
		protected boolean onTap(int index) {
			Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
			return true;
		}
	}
	
	private int getZoomForDistance(int distanceInMeter) {
		int zoom = 11;
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
		}
		return zoom;
	}
}
