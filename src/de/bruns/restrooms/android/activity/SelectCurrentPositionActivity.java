package de.bruns.restrooms.android.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import de.bruns.restrooms.android.R;
import de.bruns.restrooms.android.service.CurrentPositionService;
import de.bruns.restrooms.android.service.RestroomDataService;

public class SelectCurrentPositionActivity extends MapActivity {

	private static final String LOG_TAG = SelectCurrentPositionActivity.class
			.getSimpleName();

	private MapView mapView;
	private MapController mapController;
	private MyPositionManualOverlay myPositionManualOverlay;
	
	private RadioGroup radioGroup;
	private TextView positionDescription;

	private String positionDescriptionManual;
	private String positionDescriptionGps;

	private LocationListener locationListener;
	private LocationManager locationManager;
	private CurrentPositionService currentPositionService;

	private ProgressDialog gpsProgressDialog;
	private boolean isWaitingForGpsLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.select_current_position);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		currentPositionService = CurrentPositionService.instance(this);
		
		positionDescriptionGps = getResources().getString(R.string.position_description_gps);
		positionDescriptionManual = getResources().getString(R.string.position_description_manual);
		
		((RadioButton) findViewById(R.id.rb_position_gps)).setChecked(currentPositionService.isUseGps());
		((RadioButton) findViewById(R.id.rb_position_manual)).setChecked(!currentPositionService.isUseGps());
		
		positionDescription = (TextView) findViewById(R.id.text_position_description);
		positionDescription.setText(positionDescriptionManual);

		radioGroup = (RadioGroup) findViewById(R.id.rg_position);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (radioGroup.getCheckedRadioButtonId()) {
				case R.id.rb_position_gps:
					currentPositionService.setUseGps(true);
					positionDescription.setText(positionDescriptionGps);
					Log.v(LOG_TAG, "Use GPS position");			
					
					gpsProgressDialog = ProgressDialog.show(SelectCurrentPositionActivity.this, "Warten", "GPS wird gesucht...",
							true);
					gpsProgressDialog.setCancelable(true);
					
					final WaitForLocationTask task = new WaitForLocationTask();
					gpsProgressDialog.setOnCancelListener(new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							task.cancel(true);
						}
					});
					task.execute();
					
					break;
				case R.id.rb_position_manual:
					currentPositionService.setUseGps(false);
					positionDescription.setText(positionDescriptionManual);
					Log.v(LOG_TAG, "Use manuell position");
					break;
				default:
				}
			}
		});
		
		// buttons
		Button buttonAsList = (Button) findViewById(R.id.button_aslist);
		buttonAsList.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				startActivity(new Intent(SelectCurrentPositionActivity.this, ShowRestroomsListActivity.class));
				return true;
			}
		});
		Button buttonAsMap = (Button) findViewById(R.id.button_asmap);
		buttonAsMap.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				startActivity(new Intent(SelectCurrentPositionActivity.this, ShowRestroomsMapActivity.class));
				return true;
			}
		});

		// map view
		mapView = (MapView) findViewById(R.id.select_current_position_map);
		mapController = mapView.getController();
		int distanceInMeterToBremenCity = currentPositionService.distanceInMeterToBremenCity();
		mapController.setZoom(CurrentPositionService.getZoomForDistance(distanceInMeterToBremenCity));
		mapView.setBuiltInZoomControls(true);

		Drawable marker = getResources().getDrawable(R.drawable.you_are_here);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		
		myPositionManualOverlay = new MyPositionManualOverlay(marker);
		mapView.getOverlays().add(myPositionManualOverlay);
		mapView.invalidate();
		
		updateCurrentPosition(currentPositionService.getCurrentPosition());
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private class MyPositionManualOverlay extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> items = new ArrayList<OverlayItem>();
		private Drawable marker = null;
		private OverlayItem inDrag = null;
		private ImageView dragImage = null;
		private int xDragImageOffset = 0;
		private int yDragImageOffset = 0;
		private int xDragTouchOffset = 0;
		private int yDragTouchOffset = 0;

		public MyPositionManualOverlay(Drawable marker) {
			super(marker);
			this.marker = marker;

			dragImage = (ImageView) findViewById(R.id.drag);
			xDragImageOffset = dragImage.getDrawable().getIntrinsicWidth() / 2;
			yDragImageOffset = dragImage.getDrawable().getIntrinsicHeight();

			updatePosition(currentPositionService.getCurrentPosition());
		}
		
		public void updatePosition(GeoPoint position) {
			items.clear();
			
			OverlayItem overlayItem = new OverlayItem(
					currentPositionService.getCurrentPosition(), "Here", "Here");
			items.add(overlayItem);

			populate();
		}
		
		@Override
		protected OverlayItem createItem(int i) {
			return (items.get(i));
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);

			boundCenterBottom(marker);
		}

		@Override
		public int size() {
			return (items.size());
		}

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			boolean result = false;

			if (!currentPositionService.isUseGps()) {
				final int action = event.getAction();
				final int x = (int) event.getX();
				final int y = (int) event.getY();

				if (action == MotionEvent.ACTION_DOWN) {
					for (OverlayItem item : items) {
						Point p = new Point(0, 0);

						mapView.getProjection().toPixels(item.getPoint(), p);
						if (hitTest(item, marker, x - p.x, y - p.y)) {
							result = true;
							inDrag = item;
							items.remove(inDrag);
							populate();
							
							xDragTouchOffset = 0;
							yDragTouchOffset = 0;
							
							setDragImagePosition(p.x + mapView.getLeft(), p.y  + mapView.getTop());
							dragImage.setVisibility(View.VISIBLE);
							
							xDragTouchOffset = x - p.x;
							yDragTouchOffset = y - p.y;
							
							break;
						}
					}
				} else if (action == MotionEvent.ACTION_MOVE && inDrag != null) {
					setDragImagePosition(x + mapView.getLeft(), y  + mapView.getTop());
					result = true;
				} else if (action == MotionEvent.ACTION_UP && inDrag != null) {
					dragImage.setVisibility(View.GONE);
					
					GeoPoint currentPosition = mapView.getProjection().fromPixels(
							x - xDragTouchOffset, y - yDragTouchOffset);
					OverlayItem toDrop = new OverlayItem(currentPosition, inDrag.getTitle(),
							inDrag.getSnippet());
					
					items.add(toDrop);
					populate();
					
					inDrag = null;
					result = true;
					
					updateCurrentPosition(currentPosition);
				}
			}

			return (result || super.onTouchEvent(event, mapView));
		}

		private void setDragImagePosition(int x, int y) {
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) dragImage
					.getLayoutParams();

			lp.setMargins(x - xDragImageOffset - xDragTouchOffset, y
					- yDragImageOffset - yDragTouchOffset, 0, 0);
			dragImage.setLayoutParams(lp);
		}
	}

	public class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {
			int latE6 = (int) (loc.getLatitude() * 1E6);
			int lngE6 = (int) (loc.getLongitude() * 1E6);

			GeoPoint currentGpsPosition = new GeoPoint(latE6, lngE6);
			Log.v(LOG_TAG, "Gps position changed: " + currentGpsPosition);

			isWaitingForGpsLocation = false;
			if (currentPositionService.isUseGps()) {
				updateCurrentPosition(currentGpsPosition);
			}
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

	@Override
	protected void onResume() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
				0, locationListener);
		super.onResume();
	}
	
	
	@Override
	protected void onPause() {
		locationManager.removeUpdates(locationListener);
		
		long startTime = new Date().getTime();
		RestroomDataService.instance(this).calculateDistances();
		Log.v(LOG_TAG, "Recalculating distances took " + (new Date().getTime() - startTime) + " ms.");
		super.onPause();
	}
	
	private void updateCurrentPosition(GeoPoint currentPosition) {
		currentPositionService.updateCurrentPosition(currentPosition);
		
		if (currentPositionService.isUseGps()) {
			myPositionManualOverlay.updatePosition(currentPosition);
		}
		String logString = "Standort: " + currentPositionService.getAddressOfPosition();
		Log.v(LOG_TAG, logString);
		
		((TextView) findViewById(R.id.text_position_address)).setText(logString);
		
		int distanceInMeterToBremenCity = currentPositionService.distanceInMeterToBremenCity();
		mapController.setZoom(CurrentPositionService.getZoomForDistance(distanceInMeterToBremenCity));
		mapController.animateTo(currentPosition);
	}
	
	private class WaitForLocationTask extends AsyncTask<Void, Void, Void> {
		private boolean running = true;

		@Override
		protected Void doInBackground(Void... params) {
			isWaitingForGpsLocation = true;
			while (running && isWaitingForGpsLocation)
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			return null;
		}

		@Override
		protected void onCancelled() {
			running = false;
			gpsProgressDialog.dismiss();
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			gpsProgressDialog.dismiss();
			super.onPostExecute(result);
		}
	}
}