package de.bruns.restrooms.android.activity;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.bruns.restrooms.android.R;
import de.bruns.restrooms.android.data.OpeningImageData;
import de.bruns.restrooms.android.data.RestroomData;
import de.bruns.restrooms.android.service.TimeService;

public class RestroomListAdapter extends BaseAdapter {

	protected static final String LOG_TAG = RestroomListAdapter.class
			.getSimpleName();

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