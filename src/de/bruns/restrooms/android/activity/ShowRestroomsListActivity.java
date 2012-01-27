package de.bruns.restrooms.android.activity;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ListView;
import de.bruns.restrooms.android.R;
import de.bruns.restrooms.android.data.RestroomData;

public class ShowRestroomsListActivity extends ListActivity {

	private static final String LOG_TAG = ShowRestroomsListActivity.class.getSimpleName();
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
	    setContentView(R.layout.select_restrooms_list);
		RestroomListAdapter adapter = new RestroomListAdapter(this);
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
}
