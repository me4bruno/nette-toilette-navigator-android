package de.bruns.restrooms.android.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import de.bruns.restrooms.android.R;
import de.bruns.restrooms.android.service.CurrentPositionService;
import de.bruns.restrooms.android.service.CurrentPositionService.CurrentPositionListener;

public class SelectCurrentPositionActivity extends MapActivity {

	private static final int MANUAL_POSITION_ID = 1;
	private static final int GPS_POSITION_ID = 2;

	private static final String LOG_TAG = SelectCurrentPositionActivity.class
			.getSimpleName();

	private MapView mapView;
	private MapController mapController;
	private CurrentPositionService currentPositionService;
	private MyPositionManualOverlay myPositionManualOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_current_position);

		// map view
		mapView = (MapView) findViewById(R.id.select_current_position_map);
		mapController = mapView.getController();
		mapController.setZoom(10);
		mapView.setBuiltInZoomControls(true);

		// current position service
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());

		currentPositionService = new CurrentPositionService(locationManager,
				geocoder);
		currentPositionService
				.addCurrentPositionListener(new CurrentPositionListener() {
					@Override
					public void positionUpdated(GeoPoint currentPosition) {
						if (myPositionManualOverlay != null && currentPositionService.isUseGps()) {
							myPositionManualOverlay.updatePosition(currentPosition);
						}
						
						String logString = "Updated current position: "
								+ "Latitude = "
								+ currentPosition.getLatitudeE6()
								+ ", Longitude = "
								+ currentPosition.getLongitudeE6()
								+ ", Address = "
								+ currentPositionService.getAddressOfPosition();

						Log.v(LOG_TAG, logString);
						Toast.makeText(getBaseContext(), logString,
								Toast.LENGTH_SHORT).show();
						mapController.animateTo(currentPosition);
					}
				});

		Drawable marker = getResources().getDrawable(R.drawable.marker);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		
		myPositionManualOverlay = new MyPositionManualOverlay(marker);
		mapView.getOverlays().add(myPositionManualOverlay);

		mapView.invalidate();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MANUAL_POSITION_ID, Menu.NONE, "Manuell Position setzen");
		menu.add(1, GPS_POSITION_ID, Menu.NONE, "GPS-Position verwenden");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MANUAL_POSITION_ID:
			GeoPoint manualPosition = currentPositionService
					.getCurrentPosition();
			currentPositionService.useManualPosition(manualPosition);
			Log.v(LOG_TAG, "Use manuell position");
			break;
		case GPS_POSITION_ID:
			currentPositionService.useGpsPosition();
			Log.v(LOG_TAG, "Use GPS position");
			break;

		default:
		}

		return super.onOptionsItemSelected(item);
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
							
							setDragImagePosition(p.x, p.y);
							dragImage.setVisibility(View.VISIBLE);
							
							xDragTouchOffset = x - p.x;
							yDragTouchOffset = y - p.y;
							
							break;
						}
					}
				} else if (action == MotionEvent.ACTION_MOVE && inDrag != null) {
					setDragImagePosition(x, y);
					result = true;
				} else if (action == MotionEvent.ACTION_UP && inDrag != null) {
					dragImage.setVisibility(View.GONE);
					
					GeoPoint pt = mapView.getProjection().fromPixels(
							x - xDragTouchOffset, y - yDragTouchOffset);
					OverlayItem toDrop = new OverlayItem(pt, inDrag.getTitle(),
							inDrag.getSnippet());
					
					items.add(toDrop);
					populate();
					
					inDrag = null;
					result = true;
					
					currentPositionService.useManualPosition(pt);
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

}