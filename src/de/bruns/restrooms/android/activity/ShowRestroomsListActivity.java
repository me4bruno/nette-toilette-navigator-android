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


import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import de.bruns.restrooms.android.R;
import de.bruns.restrooms.android.data.OpeningImageData;
import de.bruns.restrooms.android.data.RestroomData;
import de.bruns.restrooms.android.service.RestroomDataService;
import de.bruns.restrooms.android.service.TimeService;

public class ShowRestroomsListActivity extends ListActivity {

	private static final String LOG_TAG = ShowRestroomsListActivity.class.getSimpleName();
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
	    setContentView(R.layout.select_restrooms_list);
	    
		RestroomDataService restroomDataService = RestroomDataService.instance(this);
		RestroomListAdapter adapter = new RestroomListAdapter(this, restroomDataService.getRestrooms());
		setListAdapter(adapter);
		
		// buttons
		Button buttonAsMap = (Button) findViewById(R.id.button_asmap);
		buttonAsMap.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				startActivity(new Intent(ShowRestroomsListActivity.this, ShowRestroomsMapActivity.class));
				return true;
			}
		});
		Button buttonPosition = (Button) findViewById(R.id.button_position);
		buttonPosition.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				startActivity(new Intent(ShowRestroomsListActivity.this, SelectCurrentPositionActivity.class));
				return true;
			}
		});
		Button buttonHelp = (Button) findViewById(R.id.button_help);
		buttonHelp.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				startActivity(new Intent(ShowRestroomsListActivity.this, ShowHelpActivity.class));
				return true;
			}
		});
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		RestroomData restroomData = (RestroomData) getListAdapter().getItem(position);
		String restroomId = restroomData.getName();

		Intent intent = new Intent(this, ShowRestroomDataActivity.class);
		intent.putExtra(ShowRestroomDataActivity.RESTROOM_ID, restroomId);
		
		Log.v(LOG_TAG, "Selected restroom: " + restroomId);
		startActivity(intent);
	}
	
	public class RestroomListAdapter extends BaseAdapter {

		private List<RestroomData> sortedRestroomData;
		private LayoutInflater inflater;
		private Context context;

		public RestroomListAdapter(Context context, List<RestroomData> sortedRestroomData) {
			this.inflater = LayoutInflater.from(context);
			this.context = context;
			this.sortedRestroomData = sortedRestroomData;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.select_restrooms_list_item, null);
				holder = new ViewHolder();
				holder.textName = (TextView) convertView
						.findViewById(R.id.textName);
				holder.iconOpen = (ImageView) convertView
						.findViewById(R.id.iconOpen);
				holder.textAddress = (TextView) convertView
						.findViewById(R.id.textAddress);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			RestroomData restroomData = sortedRestroomData.get(position);
	   
			Date currentTime = TimeService.instance().getTime();
			
			OpeningImageData openingImageData = OpeningImageData.getOpeningImageData(restroomData.isOpen(currentTime));
			int id = context.getResources().getIdentifier(openingImageData.getFilename(), "drawable",
					context.getString(R.string.package_str));
			Bitmap restroomIcon = BitmapFactory.decodeResource(context.getResources(), id);

			holder.iconOpen.setImageBitmap(restroomIcon);
			holder.textName.setText(restroomData.getName());
			holder.textAddress.setText(restroomData.getDistanceAsString() + "  -  "
					+ restroomData.getAdresse());

			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getCount() {
			return sortedRestroomData.size();
		}

		@Override
		public Object getItem(int position) {
			return sortedRestroomData.get(position);
		}
		
		private class ViewHolder {
			public ImageView iconOpen;
			public TextView textName;
			public TextView textAddress;
		}
	}

}
