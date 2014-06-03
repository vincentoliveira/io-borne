package com.innovorder.innovorder.adapter;

import java.util.List;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.listener.AddItemToChartListener;
import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.storage.BitmapStorage;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoriesListAdapter extends BaseAdapter {
	private Context context;
	private final List<CarteItem> values;
	private int layout;
	private String mediaBaseUrl;
	private AddItemToChartListener listener;
	private Typeface customFont;

	public CategoriesListAdapter(Context context, List<CarteItem> categories, int layout) {
		this.context = context;
		this.values = categories;
		this.layout = layout;
		
		listener = new AddItemToChartListener(context);
		listener.setAdapter(this);
		this.mediaBaseUrl = context.getString(R.string.ws_base) + context.getString(R.string.ws_media);

		customFont = Typeface.createFromAsset(context.getAssets(), "fonts/Aachen_bt.ttf");
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public CarteItem getItem(int position) {
		return values.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view;
		if (convertView == null) {
			view = new View(context);
			view = inflater.inflate(this.layout, null);
		} else {
			view = (View) convertView;
		}

		final CarteItem item = values.get(position);
		
		
		TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
		ImageView imageView = (ImageView) view.findViewById(R.id.dishImageView);
		
		if (item.getMediaUrl() != null && !item.getMediaUrl().equals("null")) {
			String url = mediaBaseUrl + item.getMediaUrl();
			BitmapStorage bitmapStorage = BitmapStorage.getInstance();
			bitmapStorage.loadBitmap(url, imageView);
		} else {
			imageView.setImageResource(R.drawable.ic_launcher);
		}
		
		nameTextView.setText(item.getName());
		nameTextView.setTypeface(customFont);
		
		return view;
	}

}
