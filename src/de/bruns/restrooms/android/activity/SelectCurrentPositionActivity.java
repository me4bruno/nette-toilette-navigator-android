package de.bruns.restrooms.android.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
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
import de.bruns.restrooms.android.service.CurrentPositionService.CurrentPositionListener;
import de.bruns.restrooms.android.service.RestroomDataService;

public class SelectCurrentPositionActivity extends MapActivity {

	private static final String LOG_TAG = SelectCurrentPositionActivity.class
			.getSimpleName();

	private CurrentPositionService currentPositionService;

	private MapView mapView;
	private MapController mapController;
	private MyPositionManualOverlay myPositionManualOverlay;
	
	private RadioGroup radioGroup;
	private TextView positionDescription;

	private String positionDescriptionManual;
	private String positionDescriptionGps;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.select_current_position);

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
					positionDescription.setText(positionDescriptionGps);
					currentPositionService.useGpsPosition();
					Log.v(LOG_TAG, "Use GPS position");					
					break;
				case R.id.rb_position_manual:
					positionDescription.setText(positionDescriptionManual);
					GeoPoint manualPosition = currentPositionService.getCurrentPosition();
					currentPositionService.useManualPosition(manualPosition);
					Log.v(LOG_TAG, "Use manuell position");
					break;
				default:
				}
			}
		});
		
		// address
		final TextView positionAddress = (TextView) findViewById(R.id.text_position_address);
		positionAddress.setText("Standort: " + currentPositionService.getAddressOfPosition());
		
		// map view
		mapView = (MapView) findViewById(R.id.select_current_position_map);
		mapController = mapView.getController();
		mapController.setZoom(10);
		mapView.setBuiltInZoomControls(true);

		// current position service
		currentPositionService
				.addCurrentPositionListener(new CurrentPositionListener() {
					@Override
					public void positionUpdated(GeoPoint currentPosition) {
						if (myPositionManualOverlay != null && currentPositionService.isUseGps()) {
							myPositionManualOverlay.updatePosition(currentPosition);
						}
						String logString = "Standort: " + currentPositionService.getAddressOfPosition();
						Log.v(LOG_TAG, logString);
						positionAddress.setText(logString);
						mapController.animateTo(currentPosition);
					}
				});

		Drawable marker = getResources().getDrawable(R.drawable.you_are_here);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		
		myPositionManualOverlay = new MyPositionManualOverlay(marker);
		mapView.getOverlays().add(myPositionManualOverlay);
		
		mapView.invalidate();
		
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
	}
	
	@Override
	protected void onPause() {
		long startTime = new Date().getTime();
		RestroomDataService.instance(this).calculateDistances();
		Log.v(LOG_TAG, "Recalculating distances took " + (new Date().getTime() - startTime) + " ms.");
		super.onPause();
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