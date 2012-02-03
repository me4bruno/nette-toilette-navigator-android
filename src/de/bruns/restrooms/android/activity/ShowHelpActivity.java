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


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.bruns.restrooms.android.R;
import de.bruns.restrooms.android.data.OpeningImageData;

public class ShowHelpActivity extends ListActivity {

	private static final String LINE_SEPERATOR = "\n";

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
	    setContentView(R.layout.show_help);
		setListAdapter(new HelpListAdapter(this));
		
		String thanksText = "Map Icons Collection" + LINE_SEPERATOR  + //
				"http://mapicons.nicolasmollet.com/" + LINE_SEPERATOR;
		((TextView) findViewById(R.id.txt_thanks_content)).setText(thanksText);
		
		String ueberText = "Nette Toilette Navigator" + LINE_SEPERATOR  + //
				"Prototyp für Wettbewerbe Apps4HB und Apps4DE" + LINE_SEPERATOR  + //
				"Version: 0.1.1" + LINE_SEPERATOR  + //
				"Web: http://cartopol.com/apps4de/nette-toilette-navigator/" + LINE_SEPERATOR  + //
				"Autor: Andreas Bruns" + LINE_SEPERATOR  + //
				"Mail: a.bruns(at)gmx.de" + LINE_SEPERATOR;
		((TextView) findViewById(R.id.txt_ueber_content)).setText(ueberText);
		
		// buttons
		Button buttonAsList = (Button) findViewById(R.id.button_aslist);
		buttonAsList.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				startActivity(new Intent(ShowHelpActivity.this, ShowRestroomsListActivity.class));
				return true;
			}
		});
		Button buttonAsMap = (Button) findViewById(R.id.button_asmap);
		buttonAsMap.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				startActivity(new Intent(ShowHelpActivity.this, ShowRestroomsMapActivity.class));
				return true;
			}
		});
	}

	public class HelpListAdapter extends ArrayAdapter<OpeningImageData> {
		private final Context context;

		public HelpListAdapter(Context context) {
			super(context, R.layout.show_help_list_item, OpeningImageData.ALL_IMAGES);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.show_help_list_item, parent, false);
			
			ImageView iconOpen = (ImageView) rowView.findViewById(R.id.iconOpen);
			
			OpeningImageData imageData = OpeningImageData.ALL_IMAGES[position];
			int id = context.getResources().getIdentifier(imageData.getFilename(), "drawable",
					context.getString(R.string.package_str));
			Bitmap restroomIcon = BitmapFactory.decodeResource(context.getResources(), id);
			iconOpen.setImageBitmap(restroomIcon);
			
			TextView textView = (TextView) rowView.findViewById(R.id.textName);
			textView.setText(imageData.getDescription());
			
			return rowView;
		}
		
		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}
	}
}
