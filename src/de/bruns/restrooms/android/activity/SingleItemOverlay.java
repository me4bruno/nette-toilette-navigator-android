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