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
package de.bruns.restrooms.android;

import java.util.Date;

import android.content.Context;
import android.graphics.drawable.Drawable;
import de.bruns.restrooms.android.data.RestroomData;

public class DrawableFactory {

	private static Drawable[] restroomMarkers;

	public static Drawable getRestroomMarker(Context context,
			RestroomData restroomData, Date currentTime) {
		if (restroomMarkers == null) {
			Drawable restroomGreen = context.getResources().getDrawable(
					R.drawable.restroom_green);
			restroomGreen.setBounds(0, 0, restroomGreen.getIntrinsicWidth(),
					restroomGreen.getIntrinsicHeight());

			Drawable restroomRed = context.getResources().getDrawable(
					R.drawable.restroom_red);
			restroomRed.setBounds(0, 0, restroomRed.getIntrinsicWidth(),
					restroomRed.getIntrinsicHeight());

			Drawable restroomYellow = context.getResources().getDrawable(
					R.drawable.restroom_yellow);
			restroomYellow.setBounds(0, 0, restroomYellow.getIntrinsicWidth(),
					restroomYellow.getIntrinsicHeight());

			restroomMarkers = new Drawable[] { restroomGreen, restroomRed,
					restroomYellow };
		}
		return restroomMarkers[restroomData.isOpen(currentTime)];
	}

}
