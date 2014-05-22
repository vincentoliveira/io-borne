package com.innovorder.innovorder.adapter;

import java.util.List;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.CarteItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class CartListAdapter extends BaseAdapter {
	private Context context;
	private final List<CarteItem> values;
	private Cart cart;

	public CartListAdapter(Context context, Cart cart) {
		this.context = context;
		this.cart = cart;
		this.values = cart.getItems();
	}

	@Override
	public int getCount() {
		return values.size() + 2;
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
			view = inflater.inflate(R.layout.cart_item, null);
		} else {
			view = (View) convertView;
		}

		TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
		TextView priceTextView = (TextView) view.findViewById(R.id.priceTextView);
		TextView countTextView = (TextView) view.findViewById(R.id.countTextView);
		Button removeButton = (Button) view.findViewById(R.id.removeButton);
		Button addButton = (Button) view.findViewById(R.id.addButton);
		
		if (position == getCount() - 2) { // empty line
			nameTextView.setText("");
			countTextView.setText("");
			priceTextView.setText("");
			removeButton.setVisibility(View.INVISIBLE);
			addButton.setVisibility(View.INVISIBLE);
		} else if (position == getCount() - 1) { // total
			nameTextView.setText("Total");
			nameTextView.setTypeface(Typeface.DEFAULT_BOLD);
			priceTextView.setText(formatPrice(cart.getTotal()));
			priceTextView.setTypeface(Typeface.DEFAULT_BOLD);
			countTextView.setText("");
			removeButton.setVisibility(View.INVISIBLE);
			addButton.setVisibility(View.INVISIBLE);
		} else {
			final CarteItem item = values.get(position);

			nameTextView.setTypeface(Typeface.DEFAULT);
			nameTextView.setText(item.getName());
			priceTextView.setText(formatPrice(item.getPrice()));
			countTextView.setText("1");
			removeButton.setVisibility(View.VISIBLE);
			addButton.setVisibility(View.VISIBLE);
		}
		
		return view;
	}

	@SuppressLint("DefaultLocale")
	private String formatPrice(double price) {
		return String.format("%.2f€", price);
	}
}
