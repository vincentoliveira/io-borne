package com.innovorder.innovorder.adapter;

import java.util.List;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.listener.AddItemToCartListener;
import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.storage.BitmapStorage;
import com.innovorder.innovorder.utils.PriceFormatter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter {
	private Context context;
	private final List<CarteItem> values;
	private int layout;
	private String mediaBaseUrl;
	private AddItemToCartListener listener;

	public ItemListAdapter(Context context, List<CarteItem> dishes, int layout) {
		this.context = context;
		this.values = dishes;
		this.layout = layout;
		
		listener = new AddItemToCartListener(context);
		listener.setAdapter(this);
		this.mediaBaseUrl = context.getString(R.string.ws_base) + context.getString(R.string.ws_media);
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Object getItem(int position) {
		return values.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
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
		TextView priceTextView = (TextView) view.findViewById(R.id.priceTextView);
		TextView addCartButton = (TextView) view.findViewById(R.id.addCartButton);
		ImageView imageView = (ImageView) view.findViewById(R.id.dishImageView);
		
		if (item.getMediaUrl() != null && !item.getMediaUrl().equals("null")) {
			String url = mediaBaseUrl + item.getMediaUrl();
			BitmapStorage bitmapStorage = BitmapStorage.getInstance();
			bitmapStorage.loadBitmap(url, imageView);
		}
		
		nameTextView.setText(item.getName());
		priceTextView.setText(PriceFormatter.format(item.getPrice()));
		
		addCartButton.setTag(item.getId());
		addCartButton.setOnClickListener(listener);

		Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Aachen_bt.ttf");
		nameTextView.setTypeface(custom_font);
		
		return view;
	}

}
