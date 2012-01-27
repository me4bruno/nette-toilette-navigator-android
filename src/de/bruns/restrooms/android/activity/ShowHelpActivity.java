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
import de.bruns.restrooms.android.data.RestroomData;

public class ShowHelpActivity extends ListActivity {

	// FIXME - add about data

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
	    setContentView(R.layout.show_help);
		setListAdapter(new HelpListAdapter(this));
		
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

	public class HelpListAdapter extends ArrayAdapter<String> {
		private final Context context;

		public HelpListAdapter(Context context) {
			super(context, R.layout.show_help_list_item, RestroomData.TOILET_NAMES);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.show_help_list_item, parent, false);
			
			ImageView iconOpen = (ImageView) rowView.findViewById(R.id.iconOpen);
			
			String filename = RestroomData.TOILET_IMAGES[position];
			int id = context.getResources().getIdentifier(filename, "drawable",
					context.getString(R.string.package_str));
			Bitmap restroomIcon = BitmapFactory.decodeResource(context.getResources(), id);
			iconOpen.setImageBitmap(restroomIcon);
			
			TextView textView = (TextView) rowView.findViewById(R.id.textName);
			textView.setText(RestroomData.TOILET_NAMES[position]);
			
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
