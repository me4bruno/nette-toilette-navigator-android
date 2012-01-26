package de.bruns.restrooms.android.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import de.bruns.restrooms.android.data.RestroomData;

public class ShowRestroomsListActivity extends ListActivity {

	private static final String LOG_TAG = ShowRestroomsListActivity.class.getSimpleName();
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		RestroomListAdapter adapter = new RestroomListAdapter(this);
		setListAdapter(adapter);
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
