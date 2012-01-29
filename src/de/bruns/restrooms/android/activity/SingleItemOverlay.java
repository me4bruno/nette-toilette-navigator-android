package de.bruns.restrooms.android.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

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