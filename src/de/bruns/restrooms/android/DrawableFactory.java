package de.bruns.restrooms.android;

import java.util.Date;

import de.bruns.restrooms.android.data.RestroomData;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class DrawableFactory {

	private static Drawable[] restroomMarkers;

	public static Drawable getRestroomMarker(Context context, RestroomData restroomData, Date currentTime) {
		if (restroomMarkers == null) {
			Drawable restroomGreen = context.getResources().getDrawable(R.drawable.restroom_green);
			restroomGreen.setBounds(0, 0, restroomGreen.getIntrinsicWidth(), restroomGreen.getIntrinsicHeight());
			
			Drawable restroomRed = context.getResources().getDrawable(R.drawable.restroom_red);
			restroomRed.setBounds(0, 0, restroomRed.getIntrinsicWidth(), restroomRed.getIntrinsicHeight());
			
			Drawable restroomYellow = context.getResources().getDrawable(R.drawable.restroom_yellow);
			restroomYellow.setBounds(0, 0, restroomYellow.getIntrinsicWidth(), restroomYellow.getIntrinsicHeight());
			
			restroomMarkers = new Drawable[] { restroomGreen, restroomRed, restroomYellow};
		}
		return restroomMarkers[restroomData.isOpen(currentTime)];
	}
	
}
